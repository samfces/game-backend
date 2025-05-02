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

/**
 * Modelo de jugador.
 */
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

    /**
     * Constructor vacío para JPA.
     */
    public Player() {
        //Empty constructor for JPA
    }

    /**
     * Método para obtener el ID único del jugador.
     * @return UUID ID único del jugador.
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Método para establecer el ID único del jugador.
     * @param uniqueId ID único del jugador.
     */
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Método para obtener la fecha de creación del jugador.
     * @return Date Fecha de creación del jugador.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Método para establecer la fecha de creación del jugador.
     * @param createdAt Fecha de creación del jugador.
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Método para obtener la fecha de actualización del jugador.
     * @return Date Fecha de actualización del jugador.
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Método para establecer la fecha de actualización del jugador.
     * @param updatedAt Fecha de actualización del jugador.
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Método para obtener el nombre del jugador.
     * @return String Nombre del jugador.
     */
    public String getName() {
        return name;
    }

    /**
     * Método para establecer el nombre del jugador.
     * @param name Nombre del jugador.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Método para obtener la lista de economías del jugador.
     * @return List<EconomyValue> Lista de economías del jugador.
     */
    public List<EconomyValue> getEconomies() {
        return economies;
    }

    /**
     * Método para establecer la lista de economías del jugador.
     * @param economies Lista de economías del jugador.
     */
    public void setEconomies(List<EconomyValue> economies) {
        this.economies = economies;
    }

    /**
     * Método para obtener los permisos del jugador.
     * @return BackendPermissions Permisos del jugador.
     */
    public BackendPermissions getBackendPermissions() {
        return backendPermissions;
    }

    /**
     * Método para establecer los permisos del jugador.
     * @param backendPermissions Permisos del jugador.
     */
    public void setBackendPermissions(BackendPermissions backendPermissions) {
        this.backendPermissions = backendPermissions;
    }

    /**
     * Método para obtener una economía del jugador.
     * @param type Tipo de economía.
     * @return EconomyValue Economía del jugador.
     */
    @JsonIgnore
    public EconomyValue getEconomy(EconomyType type) {
        return economies.stream().filter(economy -> economy.getType() == type)
                .findFirst()
                .orElse(null);
    }

    /**
     * Clase para construir un jugador.
     */
    public static class Builder {

        private List<EconomyType> economyTypes = new ArrayList<>();
        private Player buildingPlayer = new Player();

        /**
         * Método para establecer el ID único del jugador.
         * @param id ID único del jugador.
         * @return Builder Objeto Builder.
         */
        public Builder uniqueId(UUID id){
            buildingPlayer.uniqueId = id;
            return this;
        }

        /**
         * Método para establecer el nombre del jugador.
         * @param name Nombre del jugador.
         * @return Builder Objeto Builder.
         */
        public Builder name(String name){
            buildingPlayer.name = name;
            return this;
        }

        /**
         * Método para establecer los permisos del jugador.
         * @param permissions Permisos del jugador.
         * @return Builder Objeto Builder.
         */
        public Builder backendPermissions(BackendPermissions permissions){
            buildingPlayer.backendPermissions = permissions;
            return this;
        }

        /**
         * Método para establecer la lista de tipos de economía del jugador.
         * @param economyTypes Lista de tipos de economía.
         * @return Builder Objeto Builder.
         */
        public Builder economyTypes(List<EconomyType> economyTypes){
            this.economyTypes = economyTypes;
            return this;
        }

        /**
         * Método para construir el jugador.
         * @return Player Jugador.
         */
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
