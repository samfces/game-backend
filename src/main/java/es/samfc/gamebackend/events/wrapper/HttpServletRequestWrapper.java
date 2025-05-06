package es.samfc.gamebackend.events.wrapper;

import es.samfc.gamebackend.events.wrapper.streams.CachedServletInputStream;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpServletRequestWrapper extends jakarta.servlet.http.HttpServletRequestWrapper {

    private final byte[] cachedBody;

    public HttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream requestInputStream = request.getInputStream();
        this.cachedBody = requestInputStream.readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(this.cachedBody);
    }

    //Sobreescribimos el método getReader para que no se llame a getInputStream
    //ya que estamos usando un InputStream memoria que no necesitamos cerrar
    //y que no necesitamos hacer un buffer porque ya estamos leyendo el contenido
    //en el InputStream memoria
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    public byte[] getCachedBody() {
        return cachedBody;
    }
}
