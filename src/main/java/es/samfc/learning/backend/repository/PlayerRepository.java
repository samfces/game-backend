package es.samfc.learning.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.samfc.learning.backend.model.player.Player;

import java.util.UUID;

/**
 * Repositorio de jugadores.
 */
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    boolean existsByName(String name);

    Player findByName(String name);
}
