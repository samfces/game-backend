package es.samfc.learning.backend.model.economy;

import jakarta.persistence.*;

@Entity
@Table(name = "economy_types")
public class EconomyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id", nullable = false, updatable = false)
    private Integer id;
    private String name;
    private String plural;

    public EconomyType() {
        //Empty constructor for JPA
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public static class Builder {

        private final EconomyType buildingEconomyType;

        public Builder() {
            buildingEconomyType = new EconomyType();
        }

        public Builder id(Integer id) {
            buildingEconomyType.id = id;
            return this;
        }

        public Builder name(String name) {
            buildingEconomyType.name = name;
            return this;
        }

        public Builder plural(String plural) {
            buildingEconomyType.plural = plural;
            return this;
        }

        public EconomyType build() {
            if (buildingEconomyType.name == null || buildingEconomyType.plural == null) {
                throw new IllegalStateException("EconomyTypeBuilder incomplete");
            }
            return buildingEconomyType;
        }
    }
}
