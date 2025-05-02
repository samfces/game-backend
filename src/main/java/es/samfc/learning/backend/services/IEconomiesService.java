package es.samfc.learning.backend.services;

import es.samfc.learning.backend.model.economy.EconomyType;

import java.util.List;

public interface IEconomiesService {

    List<EconomyType> getEconomyTypes();
    EconomyType getEconomyType(Integer id);
    void saveEconomyType(EconomyType economyType);

    void deleteEconomyType(Integer id);
}
