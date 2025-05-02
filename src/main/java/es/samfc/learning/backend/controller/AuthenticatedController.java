package es.samfc.learning.backend.controller;

import es.samfc.learning.backend.model.permission.BackendPermissionType;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.services.impl.PlayerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

/**
 * Super clase para controladores que requieren autenticación.
 */
@RestController
public class AuthenticatedController {

    private final PlayerService playerService;

    /**
     * Constructor. Obtiene el servicio de jugadores de la aplicación.
     * @param playerService El servicio de jugadores.
     */
    public AuthenticatedController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Método para obtener el servicio de jugadores.
     * @return PlayerService El servicio de jugadores.
     */
    public PlayerService getPlayerService() {
        return playerService;
    }

    /**
     * Método para obtener el jugador actual de la sesión.
     * @return Optional<Player> Jugador actual.
     */
    protected Optional<Player> getPlayerFromContext(){
        return Optional.ofNullable(playerService.getPlayer(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    /**
     * Método para verificar si la sesión está autenticada.
     * @return boolean true si la sesión está autenticada.
     */
    protected boolean isAuthenticated(){
        if (SecurityContextHolder.getContext().getAuthentication() == null) return false;
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null;
    }

    /**
     * Método para verificar si el jugador actual está presente en la sesión.
     * @return boolean true si el jugador actual está presente en la sesión.
     */
    protected boolean isPlayerPresent(){
        return getPlayerFromContext().isPresent();
    }

    /**
     * Método para verificar si un jugador tiene un permiso específico.
     * @param permissionType Tipo de permiso.
     * @return boolean true si el jugador tiene el permiso.
     */
    protected boolean hasPermission(BackendPermissionType permissionType){
        Optional<Player> player = getPlayerFromContext();
        if (player.isEmpty()) return false;
        return player.get().getBackendPermissions().hasPermission(permissionType);
    }

    /**
     * Método para obtener un jugador por su ID o nombre.
     * @param playerIdOrName ID o nombre del jugador.
     * @return Player Jugador.
     */
    protected Player getPlayerByUUIDorName(String playerIdOrName) {
        Player player;
        if (playerIdOrName == null || playerIdOrName.isEmpty()) {
            return null;
        } else {
            try {
                UUID otherPlayerId = UUID.fromString(playerIdOrName);
                player = getPlayerService().getPlayer(otherPlayerId);
                if (player == null) return null;
            } catch (IllegalArgumentException e) {
                player = getPlayerService().getPlayer(playerIdOrName);
                if (player == null) return null;
            }
        }
        return player;
    }

}
