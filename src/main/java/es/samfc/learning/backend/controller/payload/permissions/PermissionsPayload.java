package es.samfc.learning.backend.controller.payload.permissions;

import es.samfc.learning.backend.model.permission.BackendPermissionType;

import java.util.List;

public class PermissionsPayload {

    private String player;
    private int value = -1;
    private List<BackendPermissionType> list = null;

    public PermissionsPayload(String player, int value, List<BackendPermissionType> list) {
        this.player = player;
        this.value = value;
        this.list = list;
    }

    public PermissionsPayload(String player, List<BackendPermissionType> list) {
        this.player = player;
        this.list = list;
    }

    public PermissionsPayload(String player, int value) {
        this.player = player;
        this.value = value;
    }

    public PermissionsPayload() {
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public List<BackendPermissionType> getList() {
        return list;
    }

    public void setList(List<BackendPermissionType> list) {
        this.list = list;
    }
}
