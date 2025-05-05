package es.samfc.gamebackend.controller.payload.auth;

/**
 * Cuerpo de la solicitud en el que se incluye el token de refresco.
 */
public class RefreshRequest {

    private String refreshToken;

    /**
     * Constructor vacío para Jackson.
     */
    public RefreshRequest() {
        //Empty constructor for Jackson
    }

    /**
     * Constructor con los datos de la solicitud.
     * @param refreshToken Token de refresco.
     */
    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Método para obtener el token de refresco.
     * @return String Token de refresco.
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Método para establecer el token de refresco.
     * @param refreshToken Token de refresco.
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
