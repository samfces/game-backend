package es.samfc.learning.backend.controller.economy;

import es.samfc.learning.backend.controller.AuthenticatedController;
import es.samfc.learning.backend.controller.payload.MessageResponse;
import es.samfc.learning.backend.controller.payload.economy.BalanceData;
import es.samfc.learning.backend.controller.payload.economy.DepositRequest;
import es.samfc.learning.backend.model.economy.EconomyType;
import es.samfc.learning.backend.model.economy.EconomyValue;
import es.samfc.learning.backend.model.permission.BackendPermissionType;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.services.impl.EconomiesService;
import es.samfc.learning.backend.services.impl.PlayerService;
import es.samfc.learning.backend.utils.controller.ControllerUtils;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para operaciones CRUD sobre el balance de un jugador.
 */

@RestController
public class BalanceController extends AuthenticatedController {

    private Logger logger = LoggerFactory.getLogger(BalanceController.class);
    private EconomiesService economiesService;

    /**
     * Constructor. Obtiene el servicio de economías de la aplicación.
     * @param economiesService El servicio de economías.
     * @param playerService El servicio de jugadores.
     */
    public BalanceController(EconomiesService economiesService, PlayerService playerService) {
        super(playerService);
        this.economiesService = economiesService;
    }

    /**
     * Método POST para depositar dinero en un jugador.
     * @param depositRequest Cuerpo de la solicitud en el que se incluyen los datos de la transacción.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con el resultado de la operación.
     */
    @ApiResponse(responseCode = "200", description = "Depósito realizado correctamente")
    @ApiResponse(responseCode = "400", description = "ID no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @PostMapping("/api/v1/balance/deposit")
    public ResponseEntity<MessageResponse> deposit(
            @RequestBody
            @Parameter(description = "Cuerpo de la solicitud en el que se incluyen los datos de la transacción", required = true)
            DepositRequest depositRequest,
            HttpServletRequest request
    ) {
        ControllerUtils.logRequest(logger, request);

        if (!isAuthenticated() || !isPlayerPresent()) return ControllerUtils.buildUnauthorizedResponse(request);
        if (!hasPermission(BackendPermissionType.EDIT_OTHERS_BALANCE)) return ControllerUtils.buildForbiddenResponse(request);

        Player otherPlayer;
        if (depositRequest.getUserId() != null) {
            otherPlayer = getPlayerService().getPlayer(depositRequest.getUserId());
            if (otherPlayer == null) return ControllerUtils.buildPlayerNotFoundResponse(request);
        } else if (depositRequest.getUserName() != null) {
            otherPlayer = getPlayerService().getPlayer(depositRequest.getUserName());
            if (otherPlayer == null) return ControllerUtils.buildPlayerNotFoundResponse(request);
        } else {
            return ControllerUtils.buildPlayerNotFoundResponse(request);
        }

        if (depositRequest.getAmount() < 0 || Double.isNaN(depositRequest.getAmount())){
            return ResponseEntity.status(400).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .payload("path", request.getRequestURI())
                            .payload("message", "Cantidad no válida")
                            .build()
            );
        }

        EconomyType type = economiesService.getEconomyType(depositRequest.getType());
        EconomyValue value = otherPlayer.getEconomy(type);
        value.increment(depositRequest.getAmount());

        getPlayerService().savePlayer(otherPlayer);

        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("player", otherPlayer.getUniqueId())
                        .payload("amount", depositRequest.getAmount())
                        .payload("type", depositRequest.getType())
                        .payload("updated", value.getValue())
                        .build()
        );
    }

    /**
     * Método POST para retirar dinero de un jugador.
     * @param depositRequest Cuerpo de la solicitud en el que se incluyen los datos de la transacción.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con el resultado de la operación.
     */
    @ApiResponse(responseCode = "200", description = "Retiro realizado correctamente")
    @ApiResponse(responseCode = "400", description = "ID no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @PostMapping("/api/v1/balance/withdraw")
    public ResponseEntity<MessageResponse> withdraw(
            @RequestBody
            @Parameter(description = "Cuerpo de la solicitud en el que se incluyen los datos de la transacción", required = true)
            DepositRequest depositRequest,
            HttpServletRequest request
    ) {
        ControllerUtils.logRequest(logger, request);

        if (!isAuthenticated() || !isPlayerPresent()) return ControllerUtils.buildUnauthorizedResponse(request);
        if (!hasPermission(BackendPermissionType.EDIT_OTHERS_BALANCE)) return ControllerUtils.buildForbiddenResponse(request);

        Player otherPlayer;
        if (depositRequest.getUserId() != null) {
            otherPlayer = getPlayerService().getPlayer(depositRequest.getUserId());
            if (otherPlayer == null) return ControllerUtils.buildPlayerNotFoundResponse(request);
        } else if (depositRequest.getUserName() != null) {
            otherPlayer = getPlayerService().getPlayer(depositRequest.getUserName());
            if (otherPlayer == null) return ControllerUtils.buildPlayerNotFoundResponse(request);
        } else {
            return ControllerUtils.buildPlayerNotFoundResponse(request);
        }

        if (depositRequest.getAmount() < 0 || Double.isNaN(depositRequest.getAmount())) return ResponseEntity.status(400).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .payload("path", request.getRequestURI())
                        .payload("message", "Cantidad no válida")
                        .build()
        );

        EconomyType type = economiesService.getEconomyType(depositRequest.getType());
        EconomyValue value = otherPlayer.getEconomy(type);
        value.decrement(depositRequest.getAmount());

        getPlayerService().savePlayer(otherPlayer);

        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("player", otherPlayer.getUniqueId())
                        .payload("amount", depositRequest.getAmount())
                        .payload("type", depositRequest.getType())
                        .payload("updated", value.getValue())
                        .build()
                );
    }

    /**
     * Método GET para obtener el balance de un jugador.
     * @param otherPlayerIdOrName ID o nombre del jugador.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con el balance del jugador.
     */
    @ApiResponse(responseCode = "200", description = "Balance obtenido correctamente")
    @ApiResponse(responseCode = "400", description = "ID o nombre no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @GetMapping({"/api/v1/balance", "/api/v1/balance/"})
    public ResponseEntity<MessageResponse> getBalance(
            @RequestParam(name = "player", required = false)
            @Parameter(description = "ID o nombre del jugador")
            String otherPlayerIdOrName,
            HttpServletRequest request
    ) {
        ControllerUtils.logRequest(logger, request);

        if (!isAuthenticated() || !isPlayerPresent()) return ControllerUtils.buildUnauthorizedResponse(request);
        if (!hasPermission(BackendPermissionType.VIEW_OTHERS_BALANCE)) return ControllerUtils.buildForbiddenResponse(request);

        Player otherPlayer = getPlayerByUUIDorName(otherPlayerIdOrName);
        if (otherPlayer == null) {
            Optional<Player> player = getPlayerFromContext();
            if (player.isEmpty()) return ControllerUtils.buildUnauthorizedResponse(request);
            otherPlayer = player.get();
        }

        List<BalanceData> balances = new ArrayList<>();
        otherPlayer.getEconomies().forEach(economyValue -> balances.add(new BalanceData(
                economyValue.getType(),
                economyValue.getValue()
        )));

        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("player", otherPlayer.getUniqueId())
                        .payload("balances", balances)
                        .build()
        );

    }

}
