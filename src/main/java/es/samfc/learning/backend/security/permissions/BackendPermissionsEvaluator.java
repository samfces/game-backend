package es.samfc.learning.backend.security.permissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import es.samfc.learning.backend.model.permission.BackendPermissionType;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.services.impl.PlayerService;

import java.io.Serializable;

public class BackendPermissionsEvaluator implements PermissionEvaluator {

    private Logger logger = LoggerFactory.getLogger(BackendPermissionsEvaluator.class);
    private PlayerService playerService;

    public BackendPermissionsEvaluator(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        logger.info("hasPermission({}, {}, {})", authentication, targetDomainObject, permission);
        Player player = playerService.getPlayer(authentication.getName());
        if (player != null) {
            BackendPermissionType type;
            try {
                type = BackendPermissionType.valueOf(permission.toString());
            } catch (IllegalArgumentException e) {
                logger.error("No existe una permisión con el nombre {}", permission);
                return false;
            }
            return player.getBackendPermissions().hasPermission(type);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}
