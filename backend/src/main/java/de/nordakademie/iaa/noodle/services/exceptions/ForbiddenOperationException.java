package de.nordakademie.iaa.noodle.services.exceptions;

public class ForbiddenOperationException extends ServiceException {
    public ForbiddenOperationException(String message) {
        super(message);
    }
}
