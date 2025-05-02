package es.samfc.learning.backend.controller.payload.economy;

public class EconomyDeleteRequest {

    private Integer id;

    public EconomyDeleteRequest() {
        //Empty constructor for Jackson
    }

    public Integer getId() {
        return id;
    }

}
