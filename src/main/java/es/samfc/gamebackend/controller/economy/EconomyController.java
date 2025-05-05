package es.samfc.gamebackend.controller.economy;

import es.samfc.gamebackend.controller.AuthenticatedController;
import es.samfc.gamebackend.controller.payload.MessageResponse;
import es.samfc.gamebackend.controller.payload.economy.EconomyDeleteRequest;
import es.samfc.gamebackend.model.economy.EconomyType;
import es.samfc.gamebackend.model.economy.EconomyValue;
import es.samfc.gamebackend.model.permission.BackendPermissionType;
import es.samfc.gamebackend.model.player.Player;
import es.samfc.gamebackend.services.impl.EconomiesService;
import es.samfc.gamebackend.services.impl.PlayerService;
import es.samfc.gamebackend.utils.controller.ControllerUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador para operaciones CRUD sobre el economía de un jugador.
 */

@RestController
public class EconomyController extends AuthenticatedController {

    private final EconomiesService economiesService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EconomyController.class);

    /**
     * Constructor. Obtiene el servicio de economías de la aplicación.
     * @param economiesService El servicio de economías.
     * @param playerService El servicio de jugadores.
     */
    public EconomyController(EconomiesService economiesService, PlayerService playerService) {
        super(playerService);
        this.economiesService = economiesService;
    }

    /**
     * Método POST para crear un tipo de economía.
     * @param economyType Tipo de economía a crear.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con el resultado de la operación.
     */
    @ApiResponse(responseCode = "201", description = "Tipo de economía creado correctamente")
    @ApiResponse(responseCode = "400", description = "ID no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @PostMapping("/api/v1/economy/create")
    public ResponseEntity<MessageResponse> create(
            @RequestBody
            @Parameter(description = "Tipo de economía a crear", required = true)
            EconomyType economyType,
            HttpServletRequest request
    ) {
        ControllerUtils.logRequest(LOGGER, request);

        if (!isAuthenticated()) return ControllerUtils.buildUnauthorizedResponse(request);
        Optional<Player> optionalPlayer = getPlayerFromContext();
        if (optionalPlayer.isEmpty()) return ControllerUtils.buildUnauthorizedResponse(request);
        Player player = optionalPlayer.get();
        if (!hasPermission(BackendPermissionType.CREATE_ECONOMIES)){
            return ControllerUtils.buildForbiddenResponse(request);
        }

        if (economyType == null) return ResponseEntity.status(400).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .payload("path", request.getRequestURI())
                        .payload("message", "Tipo de economia no válido")
                        .build()
        );

        if (economyType.getName() == null || economyType.getPlural() == null) return ResponseEntity.status(400).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .payload("path", request.getRequestURI())
                        .payload("message", "Nombre o plural del tipo de economia no válido")
                        .build()
        );

        if (player.getEconomy(economyType) != null) return ResponseEntity.status(400).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .payload("path", request.getRequestURI())
                        .payload("message", "Ya existe una economia de ese tipo")
                        .build()
        );

        economiesService.saveEconomyType(economyType);
        LOGGER.info("Cargando jugadores...");
        List<Player> players = (List<Player>) getPlayerService().getPlayers();
        LOGGER.info("Añadiendo nueva economia a {} jugadores...", players.size());
        players.forEach(p -> {
            p.getEconomies().add(new EconomyValue.Builder()
                    .setType(economyType)
                    .setValue(0.0)
                    .setPlayer(p)
                    .build()
            );
            getPlayerService().savePlayer(p);
        });

        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("player", player.getUniqueId())
                        .payload("type", economyType)
                        .build()
        );
    }

    /**
     * Método PATCH para editar un tipo de economía.
     * @param economyType Tipo de economía a editar.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con el resultado de la operación.
     */
    @ApiResponse(responseCode = "200", description = "Tipo de economía editado correctamente")
    @ApiResponse(responseCode = "400", description = "ID no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @PatchMapping("/api/v1/economy/edit")
    public ResponseEntity<MessageResponse> edit(
            @RequestBody
            @Parameter(description = "Tipo de economía a editar", required = true)
            EconomyType economyType,
            HttpServletRequest request
    ) {
        ControllerUtils.logRequest(LOGGER, request);

        if (!isAuthenticated() || !isPlayerPresent()) return ControllerUtils.buildUnauthorizedResponse(request);
        if (!hasPermission(BackendPermissionType.EDIT_ECONOMIES))
            return ControllerUtils.buildForbiddenResponse(request);

        if (economyType == null) return ResponseEntity.status(400).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .payload("path", request.getRequestURI())
                        .payload("message", "Tipo de economia no válido")
                        .build()
        );

        if (economyType.getName() == null && economyType.getPlural() == null) return ResponseEntity.status(400).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .payload("path", request.getRequestURI())
                        .payload("message", "Edición vacía")
                        .build()
        );

        EconomyType existingEconomyType = economiesService.getEconomyType(economyType.getId());
        if (existingEconomyType == null) return ResponseEntity.status(400).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .payload("path", request.getRequestURI())
                        .payload("message", "No existe una economia de ese tipo")
                        .build()
        );

        if (economyType.getName() != null) existingEconomyType.setName(economyType.getName());
        if (economyType.getPlural() != null) existingEconomyType.setPlural(economyType.getPlural());
        economiesService.saveEconomyType(economyType);

        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("type", economyType)
                        .build()
        );
    }

    /**
     * Método DELETE para eliminar un tipo de economía.
     * @param economyDeleteRequest Cuerpo de la solicitud en el que se incluye el ID del tipo de economía a eliminar.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con el resultado de la operación.
     */
    @ApiResponse(responseCode = "204", description = "Tipo de economía eliminado correctamente")
    @ApiResponse(responseCode = "400", description = "ID no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @DeleteMapping("/api/v1/economy/delete")
    public ResponseEntity<MessageResponse> delete(
            @RequestBody
            @Parameter(description = "Cuerpo de la solicitud en el que se incluye el ID del tipo de economía a eliminar", required = true)
            EconomyDeleteRequest economyDeleteRequest,
            HttpServletRequest request
    ) {
        ControllerUtils.logRequest(LOGGER, request);

        if (!isAuthenticated() || !isPlayerPresent()) return ControllerUtils.buildUnauthorizedResponse(request);
        if (!hasPermission(BackendPermissionType.DELETE_ECONOMIES))
            return ControllerUtils.buildForbiddenResponse(request);

        List<Player> players = (List<Player>) getPlayerService().getPlayers();
        for (Player p : players) {
            p.getEconomies().removeIf(ev -> ev.getType().getId().equals(economyDeleteRequest.getId()));
            getPlayerService().savePlayer(p);
        }

        economiesService.deleteEconomyType(economyDeleteRequest.getId());
        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .build()
        );
    }
}