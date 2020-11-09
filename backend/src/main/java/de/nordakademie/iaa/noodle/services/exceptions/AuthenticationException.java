package de.nordakademie.iaa.noodle.services.exceptions;

/**
 * Exception thrown, when an authentication issue occurs in one of the services.
 */
public class AuthenticationException extends ServiceException {
    public AuthenticationException(String message) {
        super(message);
    }
}
