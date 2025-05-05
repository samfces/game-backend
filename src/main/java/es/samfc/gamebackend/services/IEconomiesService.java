package es.samfc.gamebackend.services;

import es.samfc.gamebackend.model.economy.EconomyType;

import java.util.List;

/**
 * Servicio de economías.
 */
public interface IEconomiesService {

    /**
     * Método para obtener la lista de tipos de economía.
     * @return List<EconomyType> Lista de tipos de economía.
     */
    List<EconomyType> getEconomyTypes();

    /**
     * Método para obtener un tipo de economía por su ID.
     * @param id ID del tipo de economía.
     * @return EconomyType Tipo de economía.
     */
    EconomyType getEconomyType(Integer id);

    /**
     * Método para guardar un tipo de economía.
     * @param economyType Tipo de economía.
     */
    void saveEconomyType(EconomyType economyType);

    /**
     * Método para eliminar un tipo de economía.
     * @param id ID del tipo de economía.
     */
    void deleteEconomyType(Integer id);
}
