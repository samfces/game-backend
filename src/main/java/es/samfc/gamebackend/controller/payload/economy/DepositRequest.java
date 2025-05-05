package es.samfc.gamebackend.controller.payload.economy;

import java.util.UUID;

/**
 * Cuerpo de la solicitud en el que se incluyen los datos de la transacción.
 */
public class DepositRequest {

    private String userName;
    private UUID userId;
    private Double amount;
    private int type;

    /**
     * Constructor vacío para Jackson.
     */
    public DepositRequest() {
        //Empty constructor for Jackson
    }

    /**
     * Constructor con los datos de la solicitud.
     * @param userName Nombre del usuario.
     * @param userId ID del usuario.
     * @param amount Cantidad a depositar.
     * @param type ID del tipo de economía.
     */
    public DepositRequest(String userName, UUID userId, Double amount, int type) {
        this.userName = userName;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
    }

    /**
     * Método para obtener el nombre del usuario.
     * @return String Nombre del usuario.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Método para obtener el ID del usuario.
     * @return UUID ID del usuario.
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Método para obtener la cantidad a depositar.
     * @return Double Cantidad a depositar.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Método para obtener el ID del tipo de economía.
     * @return int ID del tipo de economía.
     */
    public int getType() {
        return type;
    }
}
