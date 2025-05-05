package es.samfc.learning.backend.controller.auth;

import es.samfc.learning.backend.controller.AuthenticatedController;
import es.samfc.learning.backend.controller.payload.MessageResponse;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.services.impl.PlayerService;
import es.samfc.learning.backend.utils.controller.ControllerUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthLogController extends AuthenticatedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthLogController.class);

    public AuthLogController(PlayerService playerService) {
        super(playerService);
    }

    @GetMapping("/api/v1/auth/log")
    public ResponseEntity<MessageResponse> log(HttpServletRequest request) {
        ControllerUtils.logRequest(LOGGER, request);
        if (!isAuthenticated()) return ControllerUtils.buildUnauthorizedResponse(request);
        Optional<Player> player = getPlayerFromContext();
        if (player.isEmpty()) return ControllerUtils.buildUnauthorizedResponse(request);

        int limit = 10; //TODO: Make this configurable

        return ResponseEntity.ok(new MessageResponse.Builder()
                .status(HttpStatus.OK)
                .payload("path", "/api/v1/auth/log")
                .payload("message", "Datos de inicio de sesión obtenidos correctamente")
                .payload("data", player.get().getLoginDatas().subList(0, Math.min(limit, player.get().getLoginDatas().size())))
                .build()
        );

    }
}
