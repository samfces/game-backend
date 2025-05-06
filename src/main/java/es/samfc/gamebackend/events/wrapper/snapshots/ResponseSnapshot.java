package es.samfc.gamebackend.events.wrapper.snapshots;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.util.Map;

public class ResponseSnapshot implements Serializable {
    private int status;
    private Map<String, String> headers;
    private JsonNode body;

    public ResponseSnapshot(int status, Map<String, String> headers, JsonNode body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public JsonNode getBody() {
        return body;
    }
}
