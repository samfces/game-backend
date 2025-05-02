package es.samfc.learning.backend.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import es.samfc.learning.backend.model.economy.EconomyType;
import es.samfc.learning.backend.model.economy.EconomyValue;
import es.samfc.learning.backend.model.permission.BackendPermissions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(name = "player_id", nullable = false, updatable = false)
    protected UUID uniqueId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    protected Date createdAt;

    @Column(name = "updated_at", nullable = false)
    protected Date updatedAt;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<EconomyValue> economies;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private BackendPermissions backendPermissions;

    public Player() {
        //Empty constructor for JPA
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EconomyValue> getEconomies() {
        return economies;
    }

    public void setEconomies(List<EconomyValue> economies) {
        this.economies = economies;
    }

    public BackendPermissions getBackendPermissions() {
        return backendPermissions;
    }

    public void setBackendPermissions(BackendPermissions backendPermissions) {
        this.backendPermissions = backendPermissions;
    }

    @JsonIgnore
    public EconomyValue getEconomy(EconomyType type) {
        return economies.stream().filter(economy -> economy.getType() == type)
                .findFirst()
                .orElse(null);
    }

    public static class Builder {

        private List<EconomyType> economyTypes = new ArrayList<>();
        private Player buildingPlayer = new Player();

        public Builder uniqueId(UUID id){
            buildingPlayer.uniqueId = id;
            return this;
        }

        public Builder name(String name){
            buildingPlayer.name = name;
            return this;
        }

        public Builder backendPermissions(BackendPermissions permissions){
            buildingPlayer.backendPermissions = permissions;
            return this;
        }

        public Builder economyTypes(List<EconomyType> economyTypes){
            this.economyTypes = economyTypes;
            return this;
        }

        public Player build(){
            if (buildingPlayer.uniqueId == null || buildingPlayer.name == null){
                throw new IllegalStateException("PlayerBuilder incomplete");
            }

            buildingPlayer.createdAt = new Date();
            buildingPlayer.updatedAt = new Date();
            buildingPlayer.economies = new ArrayList<>();

            if (buildingPlayer.backendPermissions.getPlayer() == null) {
                buildingPlayer.backendPermissions.setPlayer(buildingPlayer);
            }

            for (EconomyType economyType : economyTypes) {
                buildingPlayer.economies.add(new EconomyValue.Builder()
                        .setType(economyType)
                        .setValue(0.0)
                        .setPlayer(buildingPlayer)
                        .build()
                );
            }

            return buildingPlayer;
        }

    }
}
