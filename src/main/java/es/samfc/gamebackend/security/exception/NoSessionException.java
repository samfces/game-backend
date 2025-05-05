package es.samfc.gamebackend.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoSessionException extends ResponseStatusException {

    public NoSessionException() {
        super(HttpStatus.UNAUTHORIZED, "No hay una sesión iniciada");
    }
}
