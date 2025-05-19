package es.samfc.gamebackend.utils.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.samfc.gamebackend.events.wrapper.HttpServletRequestWrapper;
import es.samfc.gamebackend.events.wrapper.snapshots.RequestSnapshot;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestSnapshotUtil {

    public static RequestSnapshot from(HttpServletRequest request) {

        if (request == null) return null;

        HttpServletRequestWrapper cached = null;
        try {
            if (request instanceof HttpServletRequestWrapper wrapper) cached = wrapper;
            else cached = new HttpServletRequestWrapper(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = cached.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, cached.getHeader(name));
        }


        String body = null;
        try {
            body = new String(cached.getCachedBody(), request.getCharacterEncoding() != null
                        ? request.getCharacterEncoding()
                        : "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonBody = null;
        try {
            jsonBody = mapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new RequestSnapshot(
                cached.getMethod(),
                cached.getRequestURI(),
                cached.getParameterMap(),
                headers,
                jsonBody
        );
    }
}
