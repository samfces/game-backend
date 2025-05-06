package es.samfc.gamebackend.model.economy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import es.samfc.gamebackend.model.player.Player;
import jakarta.persistence.*;

/**
 * Modelo de valor de una economía.
 */
@Entity
@Table(name = "economy")
public class EconomyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "economy_id", nullable = false, updatable = false)
    private int economyId;

    @ManyToOne
    @JoinColumn(name = "type", nullable = false, referencedColumnName = "type_id")
    private EconomyType type;
    private Double value;

    @ManyToOne
    @JoinColumn(name = "player", nullable = false, referencedColumnName = "player_id")
    @JsonBackReference
    private Player player;

    /**
     * Método para obtener el ID de la economía.
     * @return int ID de la economía.
     */
    public int getEconomyId() {
        return economyId;
    }

    /**
     * Método para obtener el tipo de economía.
     * @return EconomyType Tipo de economía.
     */
    public EconomyType getType() {
        return type;
    }

    /**
     * Método para obtener el valor de la economía.
     * @return Double Valor de la economía.
     */
    public Double getValue() {
        return value;
    }

    /**
     * Método para obtener el jugador que posee la economía.
     * @return Player Jugador que posee la economía.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Método para establecer el tipo de economía.
     * @param type Tipo de economía.
     */
    public void setType(EconomyType type) {
        this.type = type;
    }

    /**
     * Método para establecer el valor de la economía.
     * @param value Valor de la economía.
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Método para establecer el jugador que posee la economía.
     * @param player Jugador que posee la economía.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Método para incrementar el valor de la economía.
     * @param amount Cantidad a incrementar.
     */
    public void increment(Double amount) {
        this.value += amount;
    }

    /**
     * Método para decrementar el valor de la economía.
     * @param amount Cantidad a decrementar.
     */
    public void decrement(Double amount) {
        this.value -= amount;
    }

    /**
     * Clase para construir un valor de economía.
     */
    public static class Builder {

        private final EconomyValue building = new EconomyValue();

        /**
         * Método para establecer el valor de la economía.
         * @param value Valor de la economía.
         * @return Builder Objeto Builder.
         */
        public Builder setValue(Double value) {
            building.value = value;
            return this;
        }

        /**
         * Método para establecer el tipo de economía.
         * @param type Tipo de economía.
         * @return Builder Objeto Builder.
         */
        public Builder setType(EconomyType type) {
            building.type = type;
            return this;
        }

        /**
         * Método para establecer el jugador que posee la economía.
         * @param player Jugador que posee la economía.
         * @return Builder Objeto Builder.
         */
        public Builder setPlayer(Player player){
            building.player = player;
            return this;
        }

        /**
         * Método para construir el valor de economía.
         * @return EconomyValue Valor de economía.
         */
        public EconomyValue build() {
            return building;
        }


    }
}
