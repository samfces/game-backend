package es.samfc.gamebackend.model.auth;


import es.samfc.gamebackend.model.player.Player;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "login_data")
public class LoginData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String host;
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false, referencedColumnName = "player_id")
    private Player player;

    public LoginData() {
    }

    public LoginData(String host, Date timestamp) {
        this.host = host;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public static class Builder {
        private final LoginData buildingLoginData = new LoginData();

        public Builder host(String host) {
            buildingLoginData.host = host;
            return this;
        }

        public Builder timestamp(Date timestamp) {
            buildingLoginData.timestamp = timestamp;
            return this;
        }

        public Builder player(Player player) {
            buildingLoginData.player = player;
            return this;
        }

        public LoginData build() {
            return buildingLoginData;
        }
    }
}
