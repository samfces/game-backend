package es.samfc.learning.backend.model.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "credentials")
public class PlayerCredentials {

    @Id
    private UUID uniqueId;
    private String email;
    private String password;

    public PlayerCredentials(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public PlayerCredentials() {
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Builder {

        private PlayerCredentials buildingPlayerCredentials;

        public Builder(UUID uniqueId) {
            buildingPlayerCredentials = new PlayerCredentials(uniqueId);
        }

        public Builder email(String email){
            buildingPlayerCredentials.email = email;
            return this;
        }

        public Builder password(String password){
            buildingPlayerCredentials.password = password;
            return this;
        }

        public PlayerCredentials build(){
            return buildingPlayerCredentials;
        }
    }
}
