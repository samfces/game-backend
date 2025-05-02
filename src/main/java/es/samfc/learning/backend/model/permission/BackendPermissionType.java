package es.samfc.learning.backend.model.permission;

import java.util.ArrayList;
import java.util.List;

/**
 * Tipo de permiso de un jugador.
 */
public enum BackendPermissionType {

    /**
     * Permiso de administrador.
     */
    ADMINISTRATOR(1),

    /**
     * Permiso para registrar jugadores.
     */
    REGISTER_PLAYERS(1 << 1),

    /**
     * Permiso para eliminar jugadores.
     */
    UNREGISTER_PLAYERS(1 << 2),

    /**
     * Permiso para cambiar la contraseña de un jugador.
     */
    CHANGE_PASSWORDS(1 << 3),

    /**
     * Permiso para forzar el cierre de sesión de un jugador.
     */
    FORCE_LOGOUT(1 << 4),


    /**
     * Permiso para ver el balance de un jugador.
     */
    VIEW_BALANCE(1 << 5),

    /**
     * Permiso para ver el balance de otro jugador.
     */
    VIEW_OTHERS_BALANCE(1 << 6),

    /**
     * Permiso para editar el balance de otro jugador.
     */
    EDIT_OTHERS_BALANCE(1 << 7),

    /**
     * Permiso para crear economías.
     */
    CREATE_ECONOMIES(1 << 8),

    /**
     * Permiso para editar economías.
     */
    EDIT_ECONOMIES(1 << 9),

    /**
     * Permiso para eliminar economías.
     */
    DELETE_ECONOMIES(1 << 10),

    /**
     * Permiso para ver los permisos de un jugador.
     */
    VIEW_PERMISSIONS(1 << 11),

    /**
     * Permiso para agregar permisos a un jugador.
     */
    ADD_PERMISSIONS(1 << 12),

    /**
     * Permiso para eliminar permisos de un jugador.
     */
    DELETE_PERMISSIONS(1 << 13);

    private final int value;

    /**
     * Constructor con los valores de los permisos.
     * @param value Valor de los permisos.
     */
    BackendPermissionType(int value) {
        this.value = value;
    }

    /**
     * Método para obtener el valor de los permisos.
     * @return int Valor de los permisos.
     */
    public int getValue() {
        return value;
    }

    /**
     * Clase para calcular los permisos de un jugador.
     */
    public static class Calculator {

        private Calculator() {
            throw new IllegalStateException("Illegal constructor");
        }

        /**
         * Método para convertir un array de tipos de permiso en un único valor.
         * @param permissionTypes Array de tipos de permiso.
         * @return int Valor calculado.
         */
        public static int toInt(BackendPermissionType... permissionTypes) {
            int result = 0;
            for (BackendPermissionType permissionType : permissionTypes) {
                result |= permissionType.getValue();
            }
            return result;
        }

        /**
         * Método para convertir una lista de tipos de permiso en un único valor.
         * @param permissionTypes Lista de tipos de permiso.
         * @return int Valor calculado.
         */
        public static int toInt(List<BackendPermissionType> permissionTypes) {
            return toInt(permissionTypes.toArray(new BackendPermissionType[0]));
        }

        /**
         * Método para obtener las listas de permisos de un valor.
         * @param value Valor de los permisos.
         * @return List<BackendPermissionType> Listas de permisos.
         */
        public static List<BackendPermissionType> getPermissions(int value) {
            List<BackendPermissionType> permissionTypes = new ArrayList<>();
            for (BackendPermissionType permissionType : BackendPermissionType.values()) {
                if ((permissionType.getValue() & value) != 0) {
                    permissionTypes.add(permissionType);
                }
            }
            return permissionTypes;
        }

        /**
         * Método para verificar si un valor tiene un permiso específico.
         * @param value Valor de los permisos.
         * @param permissionType Tipo de permiso.
         * @return boolean true si el valor tiene el permiso.
         */
        public static boolean hasPermission(int value, BackendPermissionType permissionType) {
            return (value & permissionType.getValue()) != 0;
        }

        /**
         * Método para agregar permisos a un valor.
         * @param initialPermissions Valor de los permisos.
         * @param permissionTypes Tipos de permiso.
         * @return int Valor calculado.
         */
        public static int addPermissions(int initialPermissions, BackendPermissionType... permissionTypes) {
            int result = initialPermissions;
            for (BackendPermissionType permissionType : permissionTypes) {
                result |= permissionType.getValue();
            }
            return result;
        }

        /**
         * Método para eliminar permisos de un valor.
         * @param initialPermissions Valor de los permisos.
         * @param permissionTypes Tipos de permiso.
         * @return int Valor calculado.
         */
        public static int removePermissions(int initialPermissions, BackendPermissionType... permissionTypes) {
            int result = initialPermissions;
            for (BackendPermissionType permissionType : permissionTypes) {
                result &= ~permissionType.getValue();
            }
            return result;
        }

    }
}
