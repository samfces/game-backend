package es.samfc.gamebackend.controller.permissions;

import es.samfc.gamebackend.controller.AuthenticatedController;
import es.samfc.gamebackend.controller.payload.MessageResponse;
import es.samfc.gamebackend.controller.payload.permissions.PermissionsPayload;
import es.samfc.gamebackend.model.permission.BackendPermissionType;
import es.samfc.gamebackend.model.player.Player;
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

import java.util.*;

/**
 * Controlador para operaciones CRUD sobre los permisos de un jugador.
 */
@RestController
public class PermissionsController extends AuthenticatedController {

    private Logger logger = LoggerFactory.getLogger(PermissionsController.class);

    /**
     * Constructor. Obtiene el servicio de jugadores de la aplicación.
     * @param playerService El servicio de jugadores.
     */
    public PermissionsController(PlayerService playerService) {
        super(playerService);
    }

    /**
     * Método GET para obtener los permisos de un jugador.
     * @param otherPlayerIdOrName ID o nombre del jugador.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con los permisos del jugador.
     */
    @ApiResponse(responseCode = "200", description = "Permisos obtenidos correctamente")
    @ApiResponse(responseCode = "400", description = "ID no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @GetMapping({"/api/v1/permissions", "/api/v1/permissions/"})
    public ResponseEntity<MessageResponse> getPermissions(
            @RequestParam(name = "player", required = false)
            @Parameter(description = "ID o nombre del jugador")
            String otherPlayerIdOrName,
            HttpServletRequest request
    ) {
        ControllerUtils.logRequest(logger, request);
        if (!isAuthenticated()) return ControllerUtils.buildUnauthorizedResponse(request);
        Optional<Player> player = getPlayerFromContext();
        if (player.isEmpty()) return ControllerUtils.buildUnauthorizedResponse(request);
        if (!hasPermission(BackendPermissionType.VIEW_PERMISSIONS))
            return ControllerUtils.buildForbiddenResponse(request);


        Player otherPlayer;
        if (otherPlayerIdOrName == null || otherPlayerIdOrName.isEmpty()) {
            otherPlayer = player.get();
        } else {
            try {
                UUID otherPlayerId = UUID.fromString(otherPlayerIdOrName);
                otherPlayer = getPlayerService().getPlayer(otherPlayerId);
                if (otherPlayer == null) return ControllerUtils.buildPlayerNotFoundResponse(request);
            } catch (IllegalArgumentException e) {
                otherPlayer = getPlayerService().getPlayer(otherPlayerIdOrName);
                if (otherPlayer == null) return ControllerUtils.buildPlayerNotFoundResponse(request);
            }
        }

        Map<String, Object> permissions = new HashMap<>();
        permissions.put("value", otherPlayer.getBackendPermissions().getPermissions());
        permissions.put("list", otherPlayer.getBackendPermissions().getPermissionList());

        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("player", otherPlayer.getUniqueId())
                        .payload("permissions", permissions)
                        .build()
        );
    }

    /**
     * Método POST para agregar permisos a un jugador.
     * @param permissionsPayload Datos de permisos a agregar.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con el resultado de la operación.
     */
    @ApiResponse(responseCode = "200", description = "Permisos agregados correctamente")
    @ApiResponse(responseCode = "400", description = "ID no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @PostMapping({"/api/v1/permissions/add", "/api/v1/permissions/add/"})
    public ResponseEntity<MessageResponse> add(
            @RequestBody
            @Parameter(description = "Datos de permisos a agregar", required = true)
            PermissionsPayload permissionsPayload,
            HttpServletRequest request
    ) {
        ControllerUtils.logRequest(logger, request);
        if (!isAuthenticated()) return ControllerUtils.buildUnauthorizedResponse(request);
        Optional<Player> player = getPlayerFromContext();
        if ( player.isEmpty()) return ControllerUtils.buildUnauthorizedResponse(request);
        if (!hasPermission(BackendPermissionType.ADD_PERMISSIONS)) return ControllerUtils.buildForbiddenResponse(request);

        Player otherPlayer = getPlayerByUUIDorName(permissionsPayload.getPlayer());
        if (otherPlayer == null) return ControllerUtils.buildPlayerNotFoundResponse(request);

        List<BackendPermissionType> permissionsList = permissionsPayload.getList();
        if (permissionsList == null || permissionsList.isEmpty()) {
            int permissionBytes = permissionsPayload.getValue();
            if (permissionBytes == -1) return ResponseEntity.status(400).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .payload("path", request.getRequestURI())
                            .payload("message", "Permisos no válidos")
                            .build()
            );

            permissionsList = BackendPermissionType.Calculator.getPermissions(permissionBytes);
            if (permissionsList.isEmpty()) return ResponseEntity.status(400).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .payload("path", request.getRequestURI())
                            .payload("message", "Permisos no válidos")
                            .build()
            );
        }

        permissionsList.forEach(p -> otherPlayer.getBackendPermissions().addPermission(p));
        getPlayerService().savePlayer(otherPlayer);

        Map<String, Object> permissions = new HashMap<>();
        permissions.put("value", otherPlayer.getBackendPermissions().getPermissions());
        permissions.put("list", otherPlayer.getBackendPermissions().getPermissionList());


        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("player", otherPlayer.getUniqueId())
                        .payload("permissions", permissions)
                        .build()
        );
    }

    /**
     * Método DELETE para eliminar permisos de un jugador.
     * @param permissionsPayload Datos de permisos a eliminar.
     * @param request Request HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta con el resultado de la operación.
     */
    @ApiResponse(responseCode = "204", description = "Permisos eliminados correctamente")
    @ApiResponse(responseCode = "400", description = "ID no válido")
    @ApiResponse(responseCode = "401", description = "No autenticado")
    @ApiResponse(responseCode = "403", description = "Sin permisos")
    @DeleteMapping("/api/v1/permissions/delete")
    public ResponseEntity<MessageResponse> delete(
            @RequestBody
            @Parameter(description = "Datos de permisos a eliminar", required = true)
            PermissionsPayload permissionsPayload,
            HttpServletRequest request
    ) {
        ControllerUtils.logRequest(logger, request);
        if (!isAuthenticated()) return ControllerUtils.buildUnauthorizedResponse(request);
        Optional<Player> player = getPlayerFromContext();
        if (player.isEmpty()) return ControllerUtils.buildUnauthorizedResponse(request);
        if (!hasPermission(BackendPermissionType.DELETE_PERMISSIONS))
            return ControllerUtils.buildForbiddenResponse(request);

        Player otherPlayer = getPlayerByUUIDorName(permissionsPayload.getPlayer());
        if (otherPlayer == null) return ControllerUtils.buildPlayerNotFoundResponse(request);

        List<BackendPermissionType> permissionsList = permissionsPayload.getList();
        if (permissionsList == null || permissionsList.isEmpty()) {
            int permissionBytes = permissionsPayload.getValue();
            if (permissionBytes == -1) return ResponseEntity.status(400).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .payload("path", request.getRequestURI())
                            .payload("message", "Permisos no válidos")
                            .build()
            );

            permissionsList = BackendPermissionType.Calculator.getPermissions(permissionBytes);
            if (permissionsList.isEmpty()) return ResponseEntity.status(400).body(
                    new MessageResponse.Builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .payload("path", request.getRequestURI())
                            .payload("message", "Permisos no válidos")
                            .build()
            );
        }

        permissionsList.forEach(p -> otherPlayer.getBackendPermissions().removePermission(p));
        getPlayerService().savePlayer(otherPlayer);

        Map<String, Object> permissions = new HashMap<>();
        permissions.put("value", otherPlayer.getBackendPermissions().getPermissions());
        permissions.put("list", otherPlayer.getBackendPermissions().getPermissionList());


        return ResponseEntity.ok(
                new MessageResponse.Builder()
                        .status(HttpStatus.OK)
                        .payload("path", request.getRequestURI())
                        .payload("player", otherPlayer.getUniqueId())
                        .payload("permissions", permissions)
                        .build()
        );
    }

}
