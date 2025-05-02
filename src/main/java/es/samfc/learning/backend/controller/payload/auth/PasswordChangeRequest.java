package es.samfc.learning.backend.controller.payload.auth;

/**
 * Cuerpo de la solicitud en el que se incluye la contraseña actual y la nueva contraseña.
 */
public class PasswordChangeRequest {

    private String oldPassword;
    private String newPassword;


    /**
     * Constructor vacío para Jackson.
     */
    public PasswordChangeRequest() {
        //Empty constructor for Jackson
    }

    /**
     * Constructor con los datos de la solicitud.
     * @param oldPassword Contraseña actual.
     * @param newPassword Nueva contraseña.
     */
    public PasswordChangeRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    /**
     * Método para obtener la contraseña actual.
     * @return String Contraseña actual.
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Método para obtener la nueva contraseña.
     * @return String Nueva contraseña.
     */
    public String getNewPassword() {
        return newPassword;
    }

}
