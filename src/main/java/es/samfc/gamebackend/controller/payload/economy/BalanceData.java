package es.samfc.gamebackend.controller.payload.economy;

import es.samfc.gamebackend.model.economy.EconomyType;

/**
 * Datos de un balance de una economía.
 */
public class BalanceData {

    private EconomyType economyType;
    private Double balance;

    /**
     * Constructor con los datos de la economía y el balance.
     * @param economyType Tipo de economía.
     * @param balance Balance de la economía.
     */
    public BalanceData(EconomyType economyType, Double balance) {
        this.economyType = economyType;
        this.balance = balance;
    }

    /**
     * Constructor vacío para Jackson.
     */
    public BalanceData() {
        //Empty constructor for Jackson
    }

    /**
     * Método para obtener el tipo de economía.
     * @return EconomyType Tipo de economía.
     */
    public EconomyType getEconomyType() {
        return economyType;
    }

    /**
     * Método para establecer el tipo de economía.
     * @param economyType Tipo de economía.
     */
    public void setEconomyType(EconomyType economyType) {
        this.economyType = economyType;
    }

    /**
     * Método para obtener el balance de la economía.
     * @return Double Balance de la economía.
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * Método para establecer el balance de la economía.
     * @param balance Balance de la economía.
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
