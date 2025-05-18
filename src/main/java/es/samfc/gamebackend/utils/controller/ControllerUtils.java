package es.samfc.gamebackend.utils.controller;

import es.samfc.gamebackend.controller.payload.MessageResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Clase para manejar las respuestas de la API REST.
 */
public class ControllerUtils {

    private ControllerUtils() {
        throw new IllegalStateException("Illegal constructor");
    }

    /**
     * Método para registrar una petición en el registro de la API REST.
     * @param logger Logger de la API REST.
     * @param request Petición HTTP.
     */
    public static void logRequest(Logger logger, HttpServletRequest request) {
        logger.info("{} {}", request.getMethod(), request.getRequestURI());
    }

    /**
     * Método para construir una respuesta de la API REST para un usuario no encontrado.
     * @param request Petición HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta de la API REST.
     */
    public static ResponseEntity<MessageResponse> buildPlayerNotFoundResponse(HttpServletRequest request) {
        return ResponseEntity.status(404).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.NOT_FOUND)
                        .payload("path", request.getRequestURI())
                        .payload("message", "Usuario no encontrado")
                        .build()
                );
    }

    /**
     * Método para construir una respuesta de la API REST para una autenticación no válida.
     * @param request Petición HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta de la API REST.
     */
    public static ResponseEntity<MessageResponse> buildUnauthorizedResponse(HttpServletRequest request) {
        return ResponseEntity.status(401).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.UNAUTHORIZED)
                        .payload("path", request.getRequestURI())
                        .payload("message", "No autenticado")
                        .build()
                );
    }

    /**
     * Método para construir una respuesta de la API REST para un acceso denegado.
     * @param request Petición HTTP.
     * @return ResponseEntity<MessageResponse> Respuesta de la API REST.
     */
    public static ResponseEntity<MessageResponse> buildForbiddenResponse(HttpServletRequest request) {
        return ResponseEntity.status(403).body(
                new MessageResponse.Builder()
                        .status(HttpStatus.FORBIDDEN)
                        .payload("path", request.getRequestURI())
                        .payload("message", "No tienes permisos para hacer esta acción")
                        .build()
                );
    }

    public static String extractRequestBody(HttpServletRequest request) throws IOException {
       return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    }

}
