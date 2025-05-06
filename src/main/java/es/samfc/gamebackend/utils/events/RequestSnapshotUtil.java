package es.samfc.gamebackend.utils.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.samfc.gamebackend.events.wrapper.HttpServletRequestWrapper;
import es.samfc.gamebackend.events.wrapper.snapshots.RequestSnapshot;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestSnapshotUtil {

    public static RequestSnapshot from(HttpServletRequest request) throws IOException {
        HttpServletRequestWrapper cached = request instanceof HttpServletRequestWrapper
                ? (HttpServletRequestWrapper) request
                : new HttpServletRequestWrapper(request);

        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = cached.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, cached.getHeader(name));
        }

        String body = new String(cached.getCachedBody(), request.getCharacterEncoding() != null
                ? request.getCharacterEncoding()
                : "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonBody = mapper.readTree(body);

        return new RequestSnapshot(
                cached.getMethod(),
                cached.getRequestURI(),
                cached.getParameterMap(),
                headers,
                jsonBody
        );
    }
}
