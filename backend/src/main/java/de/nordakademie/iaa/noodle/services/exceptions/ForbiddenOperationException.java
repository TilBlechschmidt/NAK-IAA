package de.nordakademie.iaa.noodle.services.exceptions;

/**
 * Exception thrown, when an operation is not permitted in one of the services.
 *
 * @author Noah Peeters
 */
public class ForbiddenOperationException extends ServiceException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
