package es.samfc.gamebackend.model.auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

/**
 * Modelo de credenciales de un jugador.
 */
@Entity
@Table(name = "credentials")
public class PlayerCredentials {

    @Id
    private UUID uniqueId;
    private String email;
    private String password;

    /**
     * Constructor con los datos de la credenciales.
     * @param uniqueId ID único del jugador.
     */
    public PlayerCredentials(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Constructor vacío para JPA.
     */
    public PlayerCredentials() {
        //Empty constructor for JPA
    }


    /**
     * Método para obtener el ID único del jugador.
     * @return UUID ID único del jugador.
     */
    public UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Método para establecer el ID único del jugador.
     * @param uniqueId ID único del jugador.
     */
    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    /**
     * Método para obtener el correo electrónico del jugador.
     * @return String Correo electrónico del jugador.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Método para establecer el correo electrónico del jugador.
     * @param email Correo electrónico del jugador.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Método para obtener la contraseña del jugador.
     * @return String Contraseña del jugador.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Método para establecer la contraseña del jugador.
     * @param password Contraseña del jugador.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Clase para construir una credenciales de jugador.
     */
    public static class Builder {

        private PlayerCredentials buildingPlayerCredentials;

        /**
         * Constructor con los datos de la credenciales.
         * @param uniqueId ID único del jugador.
         */
        public Builder(UUID uniqueId) {
            buildingPlayerCredentials = new PlayerCredentials(uniqueId);
        }

        /**
         * Método para establecer el correo electrónico del jugador.
         * @param email Correo electrónico del jugador.
         * @return Builder Objeto Builder.
         */
        public Builder email(String email){
            buildingPlayerCredentials.email = email;
            return this;
        }

        /**
         * Método para establecer la contraseña del jugador.
         * @param password Contraseña del jugador.
         * @return Builder Objeto Builder.
         */
        public Builder password(String password){
            buildingPlayerCredentials.password = password;
            return this;
        }

        /**
         * Método para construir la credenciales de jugador.
         * @return PlayerCredentials Credenciales de jugador.
         */
        public PlayerCredentials build(){
            return buildingPlayerCredentials;
        }
    }
}
