package es.samfc.gamebackend.controller.payload;

import es.samfc.gamebackend.events.RestEventCall;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Respuesta genérica de la API REST.
 */
@Schema(description = "Respuesta genérica de la API REST")
public class MessageResponse {

    @Schema(
            description = "Código de estado HTTP",
            example = "200, 400, 401, 403, 404, 500"
    )
    private final int status;

    @Schema(
            description = "Datos de la respuesta. Puede contener claves como 'message', 'error', 'data', etc.",
            example = "{\"message\": \"Operación exitosa, Usuario creado correctamente, etc\"}"
    )
    private final Map<String, Object> payload;

    private MessageResponse(int status, Map<String, Object> payload) {
        this.status = status;
        this.payload = payload;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public static class Builder {

        private HttpStatus status;
        private final Map<String, Object> payload = new HashMap<>();
        private RestEventCall<Object, MessageResponse> eventCall;

        public Builder status(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder payload(Map<String, Object> payload) {
            this.payload.putAll(payload);
            return this;
        }

        public Builder payload(String key, Object value) {
            this.payload.put(key, value);
            return this;
        }

        public Builder eventCall(RestEventCall<Object, MessageResponse> eventCall) {
            this.eventCall = eventCall;
            return this;
        }

        public MessageResponse build() {
            MessageResponse messageResponse = new MessageResponse(status.value(), payload);
            if (eventCall != null) {
                eventCall.setResponseData(messageResponse);
                eventCall.callEvent();
            }
            return messageResponse;
        }

    }
}
