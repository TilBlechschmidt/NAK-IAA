package de.nordakademie.iaa.noodle.services.exceptions;

/**
 * Exception thrown, when semantically invalid input is provided to one of the services.
 */
public class SemanticallyInvalidInputException extends ServiceException {
    public SemanticallyInvalidInputException(String message) {
        super(message);
    }
}
