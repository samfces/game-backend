package es.samfc.gamebackend.controller.payload.permissions;

import es.samfc.gamebackend.model.permission.BackendPermissionType;

import java.util.List;

/**
 * Datos de permisos de un jugador.
 */
public class PermissionsPayload {

    private String player;
    private int value = -1;
    private List<BackendPermissionType> list = null;

    /**
     * Constructor con los datos de permisos.
     * @param player Nombre del jugador.
     * @param value Valor de permisos.
     * @param list Lista de permisos.
     */
    public PermissionsPayload(String player, int value, List<BackendPermissionType> list) {
        this.player = player;
        this.value = value;
        this.list = list;
    }

    /**
     * Constructor con los datos de permisos.
     * @param player Nombre del jugador.
     * @param list Lista de permisos.
     */
    public PermissionsPayload(String player, List<BackendPermissionType> list) {
        this.player = player;
        this.list = list;
    }

    /**
     * Constructor con los datos de permisos.
     * @param player Nombre del jugador.
     * @param value Valor de permisos.
     */
    public PermissionsPayload(String player, int value) {
        this.player = player;
        this.value = value;
    }

    /**
     * Constructor vacío para Jackson.
     */
    public PermissionsPayload() {
        //Empty constructor for Jackson
    }

    /**
     * Método para obtener el nombre del jugador.
     * @return String Nombre del jugador.
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Método para establecer el nombre del jugador.
     * @param player Nombre del jugador.
     */
    public void setPlayer(String player) {
        this.player = player;
    }

    /**
     * Método para obtener el valor de permisos.
     * @return int Valor de permisos.
     */
    public int getValue() {
        return value;
    }

    /**
     * Método para establecer el valor de permisos.
     * @param value Valor de permisos.
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Método para obtener la lista de permisos.
     * @return List<BackendPermissionType> Lista de permisos.
     */
    public List<BackendPermissionType> getList() {
        return list;
    }

    /**
     * Método para establecer la lista de permisos.
     * @param list Lista de permisos.
     */
    public void setList(List<BackendPermissionType> list) {
        this.list = list;
    }
}
