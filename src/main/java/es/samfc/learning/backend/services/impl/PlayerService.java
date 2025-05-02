package es.samfc.learning.backend.services.impl;

import org.springframework.stereotype.Service;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.repository.PlayerRepository;
import es.samfc.learning.backend.services.IPlayerService;

import java.util.UUID;

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
