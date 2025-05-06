package es.samfc.gamebackend.controller.player;

import es.samfc.gamebackend.controller.AuthenticatedController;
import es.samfc.gamebackend.controller.payload.MessageResponse;
import es.samfc.gamebackend.model.player.Player;
import es.samfc.gamebackend.repository.CredentialsRepository;
import es.samfc.gamebackend.services.impl.PlayerService;
import es.samfc.gamebackend.utils.controller.ControllerUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Controlador para operaciones CRUD sobre el jugador actual.
 */
@RestController
public class SelfPlayerController extends AuthenticatedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelfPlayerController.class);

    private CredentialsRepository credentialsRepository; //TODO: Hacer esto en un servicio

    /**
     * Constructor. Obtiene el servicio de jugadores de la aplicación.
     * @param playerService El servicio de jugadores.
     */
    public SelfPlayerController(PlayerService playerService, CredentialsRepository credentialsRepository) {
        super(playerService);
        this.credentialsRepository = credentialsRepository;
    }

    /**
     * Método GET para obtener el jugador actual.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con el jugador actual.
     */
    @ApiResponse(responseCode = "200", description = "Jugador actual obtenido correctamente")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @GetMapping("/api/v1/self/me")
    public ResponseEntity<MessageResponse> me(HttpServletRequest request) {

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
                        .payload("email", credentialsRepository.findById(player.getUniqueId()).get().getEmail())
                        .build()
        );
    }


}
