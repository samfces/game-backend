package es.samfc.learning.backend.controller.payload.economy;

/**
 * Cuerpo de la solicitud en el que se incluye el ID del tipo de economía a eliminar.
 */
public class EconomyDeleteRequest {

    private Integer id;

    /**
     * Constructor vacío para Jackson.
     */
    public EconomyDeleteRequest() {
        //Empty constructor for Jackson
    }

    /**
     * Constructor con los datos de la solicitud.
     * @param id ID del tipo de economía.
     */
    public EconomyDeleteRequest(Integer id) {
        this.id = id;
    }

    /**
     * Método para obtener el ID del tipo de economía.
     * @return Integer ID del tipo de economía.
     */
    public Integer getId() {
        return id;
    }

}
