package es.samfc.learning.backend.model.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.UUID;

/**
 * Modelo de token de refresco.
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    private String token;
    private String username;
    private Date expirationDate;

    /**
     * Constructor vacío para JPA.
     */
    public RefreshToken() {
        //Empty constructor for JPA
    }

    /**
     * Constructor con los datos del token de refresco.
     * @param token Token de refresco.
     * @param username Nombre del usuario.
     * @param expirationDate Fecha de expiración del token.
     */
    public RefreshToken(String token, String username, Date expirationDate) {
        this.token = token;
        this.username = username;
        this.expirationDate = expirationDate;
    }

    /**
     * Método para obtener el nombre del usuario.
     * @return String Nombre del usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Método para obtener el token de refresco.
     * @return String Token de refresco.
     */
    public String getToken() {
        return token;
    }

    /**
     * Método para obtener la fecha de expiración del token.
     * @return Date Fecha de expiración del token.
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Método para generar un token de refresco aleatorio.
     * @param username Nombre del usuario.
     * @param expirationDate Fecha de expiración del token.
     * @return RefreshToken Token de refresco.
     */
    public static RefreshToken generate(String username, Date expirationDate) {
        return new RefreshToken(UUID.randomUUID().toString(), username, expirationDate);
    }
}
