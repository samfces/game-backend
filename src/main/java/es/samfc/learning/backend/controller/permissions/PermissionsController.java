package es.samfc.learning.backend.controller.permissions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import es.samfc.learning.backend.controller.AuthenticatedController;
import es.samfc.learning.backend.controller.payload.MessageResponse;
import es.samfc.learning.backend.controller.payload.permissions.PermissionsPayload;
import es.samfc.learning.backend.model.permission.BackendPermissionType;
import es.samfc.learning.backend.model.player.Player;
import es.samfc.learning.backend.utils.controller.ControllerUtils;

import java.util.*;

@RestController
public class PermissionsController extends AuthenticatedController {

    private Logger logger = LoggerFactory.getLogger(PermissionsController.class);

    @GetMapping({"/api/v1/permissions", "/api/v1/permissions/"})
    public ResponseEntity<MessageResponse> getPermissions(@RequestParam(name = "player", required = false) String otherPlayerIdOrName, HttpServletRequest request) {
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

    @PostMapping({"/api/v1/permissions/add", "/api/v1/permissions/add/"})
    public ResponseEntity<MessageResponse> add(@RequestBody PermissionsPayload permissionsPayload, HttpServletRequest request) {
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

    @DeleteMapping("/api/v1/permissions/delete")
    public ResponseEntity<MessageResponse> delete(@RequestBody PermissionsPayload permissionsPayload, HttpServletRequest request) {
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
