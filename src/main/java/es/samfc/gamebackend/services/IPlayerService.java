package es.samfc.gamebackend.services;

import es.samfc.gamebackend.model.player.Player;

import java.util.UUID;

/**
 * Servicio de jugadores.
 */
public interface IPlayerService {

    /**
     * Método para guardar un jugador.
     * @param player Jugador.
     */
    void savePlayer(Player player);

    /**
     * Método para eliminar un jugador.
     * @param player Jugador.
     */
    void deletePlayer(Player player);

    /**
     * Método para obtener un jugador por su ID.
     * @param id ID del jugador.
     * @return Player Jugador.
     */
    Player getPlayer(UUID id);

    /**
     * Método para obtener un jugador por su nombre.
     * @param name Nombre del jugador.
     * @return Player Jugador.
     */
    Player getPlayer(String name);

    /**
     * Método para obtener todas los jugadores.
     * @return Iterable<Player> Jugadores.
     */
    Iterable<Player> getPlayers();
}
