package es.samfc.learning.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.samfc.learning.backend.model.economy.EconomyType;

/**
 * Repositorio de tipos de economía.
 */
public interface EconomyTypeRepository extends JpaRepository<EconomyType, Integer> {
    void deleteEconomyTypeById(Integer id);
}
