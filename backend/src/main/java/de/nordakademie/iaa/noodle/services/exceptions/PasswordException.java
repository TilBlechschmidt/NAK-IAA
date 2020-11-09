package de.nordakademie.iaa.noodle.services.exceptions;

/**
 * Exception thrown, when an issue with a password occurs in one of the services.
 */
public class PasswordException extends ServiceException {
    public PasswordException(String message) {
        super(message);
    }
}
