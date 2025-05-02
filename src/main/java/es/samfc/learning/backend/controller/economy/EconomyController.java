package es.samfc.learning.backend.controller.economy;

import es.samfc.learning.backend.services.impl.PlayerService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import es.samfc.learning.backend.controller.AuthenticatedController;
import es.samfc.learning.backend.controller.payload.MessageResponse;
import es.samfc.learning.backend.controller.payload.economy.EconomyDeleteRequest;
import es.samfc.learning.backend.model.economy.EconomyType;
import es.samfc.learning.backend.model.economy.EconomyValue;
import es.samfc.learning.backend.model.permission.BackendPermissionType;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.services.impl.EconomiesService;
import es.samfc.learning.backend.utils.controller.ControllerUtils;

import java.util.List;
import java.util.Optional;

@RestController
public class EconomyController extends AuthenticatedController {

    private final EconomiesService economiesService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EconomyController.class);

    public EconomyController(EconomiesService economiesService, PlayerService playerService) {
        super(playerService);
        this.economiesService = economiesService;
    }

    @PostMapping("/api/v1/economy/create")
    public ResponseEntity<MessageResponse> create(@RequestBody EconomyType economyType, HttpServletRequest request) {
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

        new Thread(() -> {
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
        }).start();

        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("player", player.getUniqueId())
                        .payload("type", economyType)
                        .build()
        );
    }

    @PatchMapping("/api/v1/economy/edit")
    public ResponseEntity<MessageResponse> edit(@RequestBody EconomyType economyType, HttpServletRequest request) {
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

    @DeleteMapping("/api/v1/economy/delete")
    public ResponseEntity<MessageResponse> delete(@RequestBody EconomyDeleteRequest economyDeleteRequest, HttpServletRequest request) {
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