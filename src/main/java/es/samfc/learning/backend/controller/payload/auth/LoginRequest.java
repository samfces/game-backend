package es.samfc.learning.backend.controller.payload.auth;

/**
 * Cuerpo de la solicitud en el que se incluye el nombre de usuario y la contraseña.
 */
public class LoginRequest {

    private String username;
    private String password;

    /**
     * Constructor vacío para Jackson.
     */
    public LoginRequest() {
        //Empty constructor for Jackson
    }

    /**
     * Constructor con los datos de la solicitud.
     * @param username Nombre de usuario.
     * @param password Contraseña.
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Método para obtener el nombre de usuario.
     * @return String Nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Método para establecer el nombre de usuario.
     * @param username Nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Método para obtener la contraseña.
     * @return String Contraseña.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Método para establecer la contraseña.
     * @param password Contraseña.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}