package es.samfc.learning.backend.controller.payload;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Respuesta de la API REST.
 */
public class MessageResponse {

    private final int status;
    private final Map<String, Object> payload;

    /**
     * Constructor con los datos de la respuesta.
     * @param status Código de estado HTTP.
     * @param payload Mapa de datos.
     */
    private MessageResponse(int status, Map<String, Object> payload) {
        this.status = status;
        this.payload = payload;
    }

    /**
     * Método para obtener el código de estado HTTP.
     * @return int Código de estado HTTP.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Método para obtener el mapa de datos.
     * @return Map<String, Object> Mapa de datos.
     */
    public Map<String, Object> getPayload() {
        return payload;
    }

    /**
     * Clase para construir una respuesta de la API REST.
     */
    public static class Builder {

        private HttpStatus status;
        private final Map<String, Object> payload = new HashMap<>();

        /**
         * Método para establecer el código de estado HTTP.
         * @param status Código de estado HTTP.
         * @return Builder Objeto Builder.
         */
        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Método para establecer el mapa de datos.
         * @param payload Mapa de datos.
         * @return Builder Objeto Builder.
         */
        public Builder payload(Map<String, Object> payload) {
            this.payload.putAll(payload);
            return this;
        }

        /**
         * Método para establecer un valor en el mapa de datos.
         * @param key Clave del valor.
         * @param value Valor.
         * @return Builder Objeto Builder.
         */
        public Builder payload(String key, Object value) {
            this.payload.put(key, value);
            return this;
        }

        /**
         * Método para construir la respuesta de la API REST.
         * @return MessageResponse Respuesta de la API REST.
         */
        public MessageResponse build() {
            return new MessageResponse(status.value(), payload);
        }

    }
}