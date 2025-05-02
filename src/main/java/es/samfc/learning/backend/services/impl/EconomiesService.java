package es.samfc.learning.backend.services.impl;

import org.springframework.stereotype.Service;
import es.samfc.learning.backend.model.economy.EconomyType;
import es.samfc.learning.backend.repository.EconomyTypeRepository;
import es.samfc.learning.backend.services.IEconomiesService;

import java.util.List;

@Service
public class EconomiesService implements IEconomiesService {

    private final EconomyTypeRepository economyTypeRepository;

    public EconomiesService(EconomyTypeRepository economyTypeRepository) {
        this.economyTypeRepository = economyTypeRepository;
    }

    @Override
    public List<EconomyType> getEconomyTypes() {
        return economyTypeRepository.findAll();
    }

    @Override
    public EconomyType getEconomyType(Integer id) {
        return economyTypeRepository.findById(id).orElse(null);
    }

    @Override
    public void saveEconomyType(EconomyType economyType) {
        economyTypeRepository.save(economyType);
    }

    @Override
    public void deleteEconomyType(Integer id) {
        economyTypeRepository.deleteById(id);
    }
}
