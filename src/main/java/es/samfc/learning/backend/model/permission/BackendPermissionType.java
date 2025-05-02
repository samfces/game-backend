package es.samfc.learning.backend.model.permission;

import java.util.ArrayList;
import java.util.List;

public enum BackendPermissionType {

    ADMINISTRATOR(1),
    REGISTER_PLAYERS(1 << 1),
    UNREGISTER_PLAYERS(1 << 2),
    CHANGE_PASSWORDS(1 << 3),
    FORCE_LOGOUT(1 << 4),

    VIEW_BALANCE(1 << 5),
    VIEW_OTHERS_BALANCE(1 << 6),
    EDIT_OTHERS_BALANCE(1 << 7),

    CREATE_ECONOMIES(1 << 8),
    EDIT_ECONOMIES(1 << 9),
    DELETE_ECONOMIES(1 << 10),

    VIEW_PERMISSIONS(1 << 11),
    ADD_PERMISSIONS(1 << 12),
    DELETE_PERMISSIONS(1 << 13);

    private final int value;

    BackendPermissionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static class Calculator {

        public static int toInt(BackendPermissionType... permissionTypes) {
            int result = 0;
            for (BackendPermissionType permissionType : permissionTypes) {
                result |= permissionType.getValue();
            }
            return result;
        }

        public static int toInt(List<BackendPermissionType> permissionTypes) {
            return toInt(permissionTypes.toArray(new BackendPermissionType[0]));
        }

        public static List<BackendPermissionType> getPermissions(int value) {
            List<BackendPermissionType> permissionTypes = new ArrayList<>();
            for (BackendPermissionType permissionType : BackendPermissionType.values()) {
                if ((permissionType.getValue() & value) != 0) {
                    permissionTypes.add(permissionType);
                }
            }
            return permissionTypes;
        }

        public static boolean hasPermission(int value, BackendPermissionType permissionType) {
            return (value & permissionType.getValue()) != 0;
        }

        public static int addPermissions(int initialPermissions, BackendPermissionType... permissionTypes) {
            int result = initialPermissions;
            for (BackendPermissionType permissionType : permissionTypes) {
                result |= permissionType.getValue();
            }
            return result;
        }

        public static int removePermissions(int initialPermissions, BackendPermissionType... permissionTypes) {
            int result = initialPermissions;
            for (BackendPermissionType permissionType : permissionTypes) {
                result &= ~permissionType.getValue();
            }
            return result;
        }
    }
}
