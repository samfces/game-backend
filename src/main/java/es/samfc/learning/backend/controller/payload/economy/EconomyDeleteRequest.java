package es.samfc.learning.backend.controller.payload.economy;

public class EconomyDeleteRequest {

    private Integer id;

    public EconomyDeleteRequest() {
        //Empty constructor for Jackson
    }

    public EconomyDeleteRequest(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}
