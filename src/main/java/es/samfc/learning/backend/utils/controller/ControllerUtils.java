package es.samfc.learning.backend.utils.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import es.samfc.learning.backend.controller.payload.MessageResponse;

public class ControllerUtils {

    public static void logRequest(Logger logger, HttpServletRequest request) {
        logger.info("{} {}", request.getMethod(), request.getRequestURI());
    }

    public static ResponseEntity<MessageResponse> buildPlayerNotFoundResponse(HttpServletRequest request) {
        return ResponseEntity.status(404).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.NOT_FOUND)
                        .payload("path", request.getRequestURI())
                        .payload("message", "Usuario no encontrado")
                        .build()
        );
    }

    public static ResponseEntity<MessageResponse> buildUnauthorizedResponse(HttpServletRequest request) {
        return ResponseEntity.status(401).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .payload("path", request.getRequestURI())
                        .payload("message", "No autenticado")
                        .build()
        );
    }

    public static ResponseEntity<MessageResponse> buildForbiddenResponse(HttpServletRequest request) {
        return ResponseEntity.status(403).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.FORBIDDEN)
                        .payload("path", request.getRequestURI())
                        .payload("message", "No tienes permisos para hacer esta acción")
                        .build()
        );
    }

}
