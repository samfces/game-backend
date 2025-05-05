package es.samfc.gamebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.samfc.gamebackend.model.player.Player;

import java.util.UUID;

/**
 * Repositorio de jugadores.
 */
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    boolean existsByName(String name);

    Player findByName(String name);
}
