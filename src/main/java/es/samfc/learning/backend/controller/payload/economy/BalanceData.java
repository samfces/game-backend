package es.samfc.learning.backend.controller.payload.economy;

import es.samfc.learning.backend.model.economy.EconomyType;

public class BalanceData {

    private EconomyType economyType;
    private Double balance;

    public BalanceData(EconomyType economyType, Double balance) {
        this.economyType = economyType;
        this.balance = balance;
    }

    public BalanceData() {
    }

    public EconomyType getEconomyType() {
        return economyType;
    }

    public void setEconomyType(EconomyType economyType) {
        this.economyType = economyType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
