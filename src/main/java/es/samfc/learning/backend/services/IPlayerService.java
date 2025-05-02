package es.samfc.learning.backend.services;

import es.samfc.learning.backend.model.player.Player;

import java.util.UUID;

public interface IPlayerService {

    void savePlayer(Player player);

    void deletePlayer(Player player);

    Player getPlayer(UUID id);

    Player getPlayer(String name);

    Iterable<Player> getPlayers();
}
