package es.samfc.learning.backend.controller.payload.auth;

/**
 * Cuerpo de la solicitud en el que se incluye el nombre del usuario y la contraseña.
 */
public class RegisterRequest {

    private String username;
    private String email;
    private String password;

    /**
     * Constructor vacío para Jackson.
     */
    public RegisterRequest() {
        //Empty constructor for Jackson
    }

    /**
     * Constructor con los datos de la solicitud.
     * @param username Nombre de usuario.
     * @param email Correo electrónico.
     * @param password Contraseña.
     */
    public RegisterRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
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
     * Método para obtener el correo electrónico.
     * @return String Correo electrónico.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Método para establecer el correo electrónico.
     * @param email Correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
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