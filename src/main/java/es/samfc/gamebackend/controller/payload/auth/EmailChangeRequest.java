package es.samfc.gamebackend.controller.payload.auth;

/**
 * Cuerpo de la solicitud en el que se incluye la contraseña y la nueva dirección de correo electrónico.
 */
public class EmailChangeRequest {

    private String password;
    private String email;

    /**
     * Constructor vacío para Jackson.
     */
    public EmailChangeRequest() {
        //Empty constructor for Jackson
    }

    /**
     * Constructor con los datos de la solicitud.
     * @param email Nueva dirección de correo electrónico.
     */
    public EmailChangeRequest(String email) {
        this.email = email;
    }

    /**
     * Método para obtener la nueva dirección de correo electrónico.
     * @return String Nueva dirección de correo electrónico.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Método para obtener la contraseña.
     * @return String Contraseña.
     */
    public String getPassword() {
        return password;
    }

}
