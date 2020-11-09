package de.nordakademie.iaa.noodle.services.exceptions;

/**
 * Exception thrown, when an issue with a JWT token occurs in one of the services.
 */
public class JWTException extends ServiceException {
    public JWTException(String message) {
        super(message);
    }
}
