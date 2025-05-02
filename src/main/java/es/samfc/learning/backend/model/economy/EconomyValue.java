package es.samfc.learning.backend.model.economy;

import jakarta.persistence.*;
import es.samfc.learning.backend.model.player.Player;

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
    private Player player;

    public int getEconomyId() {
        return economyId;
    }

    public EconomyType getType() {
        return type;
    }

    public Double getValue() {
        return value;
    }

    public Player getPlayer() {
        return player;
    }

    public void setType(EconomyType type) {
        this.type = type;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void increment(Double amount) {
        this.value += amount;
    }

    public void decrement(Double amount) {
        this.value -= amount;
    }

    public static class Builder {

        private EconomyValue building = new EconomyValue();

        public Builder setValue(Double value) {
            building.value = value;
            return this;
        }

        public Builder setType(EconomyType type) {
            building.type = type;
            return this;
        }

        public Builder setPlayer(Player player){
            building.player = player;
            return this;
        }

        public EconomyValue build() {
            return building;
        }


    }
}
