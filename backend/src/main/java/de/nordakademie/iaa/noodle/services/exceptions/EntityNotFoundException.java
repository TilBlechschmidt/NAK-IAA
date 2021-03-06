package de.nordakademie.iaa.noodle.services.exceptions;

/**
 * Exception thrown, when an entity was not found in one of the services.
 *
 * @author Noah Peeters
 */
public class EntityNotFoundException extends ServiceException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
