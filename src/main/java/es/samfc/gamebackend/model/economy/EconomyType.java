package es.samfc.gamebackend.model.economy;

import jakarta.persistence.*;

/**
 * Modelo de tipo de economía.
 */
@Entity
@Table(name = "economy_types")
public class EconomyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id", nullable = false, updatable = false)
    private Integer id;
    private String name;
    private String plural;

    /**
     * Constructor vacío para JPA.
     */
    public EconomyType() {
        //Empty constructor for JPA
    }

    /**
     * Método para obtener el ID del tipo de economía.
     * @return Integer ID del tipo de economía.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Método para establecer el ID del tipo de economía.
     * @param id ID del tipo de economía.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Método para obtener el nombre del tipo de economía.
     * @return String Nombre del tipo de economía.
     */
    public String getName() {
        return name;
    }

    /**
     * Método para establecer el nombre del tipo de economía.
     * @param name Nombre del tipo de economía.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Método para obtener el plural del tipo de economía.
     * @return String Plural del tipo de economía.
     */
    public String getPlural() {
        return plural;
    }

    /**
     * Método para establecer el plural del tipo de economía.
     * @param plural Plural del tipo de economía.
     */
    public void setPlural(String plural) {
        this.plural = plural;
    }

    /**
     * Clase para construir un tipo de economía.
     */
    public static class Builder {

        private final EconomyType buildingEconomyType;

        /**
         * Constructor del builder.
         */
        public Builder() {
            buildingEconomyType = new EconomyType();
        }

        /**
         * Método para establecer el ID del tipo de economía.
         * @param id ID del tipo de economía.
         * @return Builder Objeto Builder.
         */
        public Builder id(Integer id) {
            buildingEconomyType.id = id;
            return this;
        }

        /**
         * Método para establecer el nombre del tipo de economía.
         * @param name Nombre del tipo de economía.
         * @return Builder Objeto Builder.
         */
        public Builder name(String name) {
            buildingEconomyType.name = name;
            return this;
        }

        /**
         * Método para establecer el plural del tipo de economía.
         * @param plural Plural del tipo de economía.
         * @return Builder Objeto Builder.
         */
        public Builder plural(String plural) {
            buildingEconomyType.plural = plural;
            return this;
        }

        /**
         * Método para construir el tipo de economía.
         * @return EconomyType Tipo de economía.
         */
        public EconomyType build() {
            if (buildingEconomyType.name == null || buildingEconomyType.plural == null) {
                throw new IllegalStateException("EconomyTypeBuilder incomplete");
            }
            return buildingEconomyType;
        }
    }
}
