package es.samfc.gamebackend.events.wrapper.streams;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CachedServletInputStream extends ServletInputStream {

    private final ByteArrayInputStream buffer;

    public CachedServletInputStream(byte[] body) {
        this.buffer = new ByteArrayInputStream(body);
    }

    @Override
    public int read() throws IOException {
        return buffer.read();
    }

    @Override
    public boolean isFinished() {
        return buffer.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true; // siempre listo porque es memoria
    }

    @Override
    public void setReadListener(ReadListener listener) {
        // No es necesario implementar para uso básico
        throw new UnsupportedOperationException();
    }
}
