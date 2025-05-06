package es.samfc.gamebackend.events.wrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.samfc.gamebackend.events.wrapper.snapshots.ResponseSnapshot;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpServletResponseWrapper extends jakarta.servlet.http.HttpServletResponseWrapper {

    private final Map<String, String> headers = new HashMap<>();
    private int status = SC_OK;
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final ServletOutputStream outputStream;
    private final PrintWriter writer;

    public HttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        outputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                buffer.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                // no-op
            }
        };
        writer = new PrintWriter(new OutputStreamWriter(buffer, StandardCharsets.UTF_8));
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        writer.flush();
        super.flushBuffer();
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        this.status = sc;
    }

    @Override
    public void setHeader(String name, String value) {
        super.setHeader(name, value);
        headers.put(name, value);
    }

    public ResponseSnapshot toSnapshot() throws IOException {
        writer.flush();
        String body = buffer.toString(StandardCharsets.UTF_8);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonBody = mapper.readTree(body);
        return new ResponseSnapshot(status, headers, jsonBody);
    }

    public void copyBodyToResponse() throws IOException {
        getResponse().getOutputStream().write(buffer.toByteArray());
    }
}
