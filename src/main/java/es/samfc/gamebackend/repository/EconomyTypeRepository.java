package es.samfc.gamebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.samfc.gamebackend.model.economy.EconomyType;

/**
 * Repositorio de tipos de economía.
 */
public interface EconomyTypeRepository extends JpaRepository<EconomyType, Integer> {
    void deleteEconomyTypeById(Integer id);
}
