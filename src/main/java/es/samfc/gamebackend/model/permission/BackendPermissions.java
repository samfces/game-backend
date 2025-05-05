package es.samfc.gamebackend.model.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import es.samfc.gamebackend.model.player.Player;

import java.util.List;

/**
 * Modelo de permisos de un jugador.
 */
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

    /**
     * Método para obtener los permisos del jugador.
     * @return int Permisos del jugador.
     */
    public int getPermissions() {
        return permissions;
    }

    /**
     * Método para establecer los permisos del jugador.
     * @param permissions Permisos del jugador.
     */
    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    /**
     * Método para obtener el jugador que posee los permisos.
     * @return Player Jugador que posee los permisos.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Método para establecer el jugador que posee los permisos.
     * @param player Jugador que posee los permisos.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Método para verificar si un jugador tiene un permiso específico.
     * @param type Tipo de permiso.
     * @return boolean true si el jugador tiene el permiso.
     */
    public boolean hasPermission(BackendPermissionType type) {
        if (BackendPermissionType.Calculator.hasPermission(getPermissions(), BackendPermissionType.ADMINISTRATOR)) {
            return true;
        }
        return BackendPermissionType.Calculator.hasPermission(getPermissions(), type);
    }

    /**
     * Método para agregar un permiso al jugador.
     * @param type Tipo de permiso.
     * @return boolean true si el permiso se agregó correctamente.
     */
    @JsonIgnore
    public void addPermission(BackendPermissionType type) {
        this.permissions = BackendPermissionType.Calculator.addPermissions(this.permissions, type);
    }

    /**
     * Método para eliminar un permiso del jugador.
     * @param type Tipo de permiso.
     * @return boolean true si el permiso se eliminó correctamente.
     */
    @JsonIgnore
    public void removePermission(BackendPermissionType type) {
        this.permissions = BackendPermissionType.Calculator.removePermissions(this.permissions, type);
    }

    /**
     * Método para obtener la lista de permisos del jugador.
     * @return List<BackendPermissionType> Lista de permisos del jugador.
     */
    @JsonIgnore
    public List<BackendPermissionType> getPermissionList(){
        return BackendPermissionType.Calculator.getPermissions(this.permissions);
    }

    /**
     * Clase para construir un permiso de jugador.
     */
    public static class Builder {

        private final BackendPermissions buildingPermissions = new BackendPermissions();

        /**
         * Método para establecer el jugador que posee los permisos.
         * @param player Jugador que posee los permisos.
         * @return Builder Objeto Builder.
         */
        public Builder player(Player player){
            buildingPermissions.player = player;
            return this;
        }

        /**
         * Método para establecer los permisos del jugador.
         * @param permissions Permisos del jugador.
         * @return Builder Objeto Builder.
         */
        public Builder permissions(int permissions){
            buildingPermissions.permissions = permissions;
            return this;
        }

        /**
         * Método para establecer los permisos del jugador.
         * @param permissions Permisos del jugador.
         * @return Builder Objeto Builder.
         */
        public Builder permissions(BackendPermissionType... permissions){
            buildingPermissions.permissions = BackendPermissionType.Calculator.toInt(permissions);
            return this;
        }

        /**
         * Método para establecer los permisos del jugador.
         * @param permissions Permisos del jugador.
         * @return Builder Objeto Builder.
         */
        public Builder permissions(List<BackendPermissionType> permissions) {
            buildingPermissions.permissions = BackendPermissionType.Calculator.toInt(permissions);
            return this;
        }

        /**
         * Método para construir el permiso de jugador.
         * @return BackendPermissions Permiso de jugador.
         */
        public BackendPermissions build(){
            return buildingPermissions;
        }
    }
}
