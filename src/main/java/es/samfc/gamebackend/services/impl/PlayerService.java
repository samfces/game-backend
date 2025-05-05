package es.samfc.gamebackend.services.impl;

import org.springframework.stereotype.Service;
import es.samfc.gamebackend.model.player.Player;
import es.samfc.gamebackend.repository.PlayerRepository;
import es.samfc.gamebackend.services.IPlayerService;

import java.util.UUID;

/**
 * Implementación del servicio de jugadores.
 */
@Service
public class PlayerService implements IPlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void savePlayer(Player player) {
        playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }

    @Override
    public Player getPlayer(UUID id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    public Player getPlayer(String name) {
        return playerRepository.findByName(name);
    }

    @Override
    public Iterable<Player> getPlayers() {
        return playerRepository.findAll();
    }
}
