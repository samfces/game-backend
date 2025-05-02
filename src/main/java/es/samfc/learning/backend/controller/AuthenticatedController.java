package es.samfc.learning.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import es.samfc.learning.backend.model.permission.BackendPermissionType;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.services.impl.PlayerService;

import java.util.Optional;
import java.util.UUID;

@RestController
public class AuthenticatedController {

    @Autowired private PlayerService playerService;

    public PlayerService getPlayerService() {
        return playerService;
    }

    protected Optional<Player> getPlayerFromContext(){
        return Optional.ofNullable(playerService.getPlayer(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    protected boolean isAuthenticated(){
        if (SecurityContextHolder.getContext().getAuthentication() == null) return false;
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null;
    }

    protected boolean isPlayerPresent(){
        return getPlayerFromContext().isPresent();
    }

    protected boolean hasPermission(BackendPermissionType permissionType){
        Optional<Player> player = getPlayerFromContext();
        if (player.isEmpty()) return false;
        return player.get().getBackendPermissions().hasPermission(permissionType);
    }

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
