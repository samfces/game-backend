package es.samfc.learning.backend.model.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import es.samfc.learning.backend.model.player.Player;

import java.util.List;

@Entity
@Table(name = "permissions")
public class BackendPermissions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permissions_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "permissions", nullable = false)
    private int permissions;

    @OneToOne
    @JoinColumn(name = "player", nullable = false, referencedColumnName = "player_id")
    private Player player;

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean hasPermission(BackendPermissionType type) {
        if (BackendPermissionType.Calculator.hasPermission(getPermissions(), BackendPermissionType.ADMINISTRATOR)) {
            return true;
        }
        return BackendPermissionType.Calculator.hasPermission(getPermissions(), type);
    }

    @JsonIgnore
    public void addPermission(BackendPermissionType type) {
        this.permissions = BackendPermissionType.Calculator.addPermissions(this.permissions, type);
    }

    @JsonIgnore
    public void removePermission(BackendPermissionType type) {
        this.permissions = BackendPermissionType.Calculator.removePermissions(this.permissions, type);
    }

    @JsonIgnore
    public List<BackendPermissionType> getPermissionList(){
        return BackendPermissionType.Calculator.getPermissions(this.permissions);
    }

    public static class Builder {

        private final BackendPermissions buildingPermissions = new BackendPermissions();

        public Builder player(Player player){
            buildingPermissions.player = player;
            return this;
        }

        public Builder permissions(int permissions){
            buildingPermissions.permissions = permissions;
            return this;
        }

        public Builder permissions(BackendPermissionType... permissions){
            buildingPermissions.permissions = BackendPermissionType.Calculator.toInt(permissions);
            return this;
        }

        public Builder permissions(List<BackendPermissionType> permissions) {
            buildingPermissions.permissions = BackendPermissionType.Calculator.toInt(permissions);
            return this;
        }

        public BackendPermissions build(){
            return buildingPermissions;
        }
    }
}
