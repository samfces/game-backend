package es.samfc.learning.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.samfc.learning.backend.model.economy.EconomyType;

public interface EconomyTypeRepository extends JpaRepository<EconomyType, Integer> {
    void deleteEconomyTypeById(Integer id);
}
