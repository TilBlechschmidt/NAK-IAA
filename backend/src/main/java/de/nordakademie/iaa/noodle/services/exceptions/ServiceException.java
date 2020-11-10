package de.nordakademie.iaa.noodle.services.exceptions;

/**
 * Abstract exception for services.
 *
 * @author Noah Peeters
 */
public abstract class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }
}
