package es.samfc.learning.backend.model.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    private String token;
    private String username;
    private Date expirationDate;

    public RefreshToken() {
    }

    public RefreshToken(String token, String username, Date expirationDate) {
        this.token = token;
        this.username = username;
        this.expirationDate = expirationDate;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public static RefreshToken generate(String username, Date expirationDate) {
        return new RefreshToken(UUID.randomUUID().toString(), username, expirationDate);
    }
}
