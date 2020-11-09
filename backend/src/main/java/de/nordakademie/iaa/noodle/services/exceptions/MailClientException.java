package de.nordakademie.iaa.noodle.services.exceptions;

/**
 * Exception thrown, when an issue with sending mails occurs in one of the services.
 */
public class MailClientException extends ServiceException {
    public MailClientException(String message) {
        super(message);
    }
}
