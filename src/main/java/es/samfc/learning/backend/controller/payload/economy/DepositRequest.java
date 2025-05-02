package es.samfc.learning.backend.controller.payload.economy;

import java.util.UUID;

public class DepositRequest {


    private String userName;
    private UUID userId;
    private Double amount;
    private int type;

    public DepositRequest() {
    }

    public DepositRequest(String userName, UUID userId, Double amount, int type) {
        this.userName = userName;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }


    public UUID getUserId() {
        return userId;
    }

    public Double getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }
}
