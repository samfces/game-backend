package es.samfc.learning.backend.controller.player;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import es.samfc.learning.backend.controller.AuthenticatedController;
import es.samfc.learning.backend.controller.payload.MessageResponse;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.utils.controller.ControllerUtils;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SelfPlayerController extends AuthenticatedController {

    private Logger logger = LoggerFactory.getLogger(SelfPlayerController.class);

    @GetMapping("/api/v1/self/me")
    public ResponseEntity<MessageResponse> me(HttpServletRequest request) {
        ControllerUtils.logRequest(logger, request);
        if (!isAuthenticated()) return ControllerUtils.buildUnauthorizedResponse(request);

        Player player = getPlayerService().getPlayer(SecurityContextHolder.getContext().getAuthentication().getName());
        Map<String, Object> data = new HashMap<>();
        data.put("name", player.getName());
        data.put("uuid", player.getUniqueId());
        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("player", data)
                        .build()
        );
    }


}
