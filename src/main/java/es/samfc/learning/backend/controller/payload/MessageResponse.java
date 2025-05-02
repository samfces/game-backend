package es.samfc.learning.backend.controller.payload;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class MessageResponse {

    private final int status;
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

        public MessageResponse build() {
            return new MessageResponse(status.value(), payload);
        }

    }
}