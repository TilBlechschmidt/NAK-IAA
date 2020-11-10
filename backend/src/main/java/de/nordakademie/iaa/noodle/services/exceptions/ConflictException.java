package de.nordakademie.iaa.noodle.services.exceptions;

/**
 * Exception thrown, when a conflict occurs in one of the services.
 *
 * @author Noah Peeters
 */
public class ConflictException extends ServiceException {
    public ConflictException(String message) {
        super(message);
    }
}
