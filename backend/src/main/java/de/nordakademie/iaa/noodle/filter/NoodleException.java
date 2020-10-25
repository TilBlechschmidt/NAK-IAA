package de.nordakademie.iaa.noodle.filter;

import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public class NoodleException extends RuntimeException {
    private final HttpStatus status;

    private NoodleException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static NoodleException badRequest(String message) {
        return new NoodleException(BAD_REQUEST, message);
    }

    public static NoodleException conflict(String message) {
        return new NoodleException(CONFLICT, message);
    }

    public static NoodleException unauthorized(String message) {
        return new NoodleException(UNAUTHORIZED, message);
    }

    public static NoodleException serviceUnavailable(String message) {
        return new NoodleException(SERVICE_UNAVAILABLE, message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
