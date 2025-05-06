package es.samfc.gamebackend.events.wrapper.snapshots;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.util.Map;

public class RequestSnapshot implements Serializable {

    private String method;
    private String path;
    private Map<String, String[]> parameters;
    private Map<String, String> headers;
    private JsonNode body;

    public RequestSnapshot(String method, String path, Map<String, String[]> parameters,
                           Map<String, String> headers, JsonNode body) {
        this.method = method;
        this.path = path;
        this.parameters = parameters;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String[]> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public JsonNode getBody() {
        return body;
    }

    public void setBody(JsonNode body) {
        this.body = body;
    }
}
