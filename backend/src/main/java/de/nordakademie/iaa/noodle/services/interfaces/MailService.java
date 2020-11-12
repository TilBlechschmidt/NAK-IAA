package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.MailClientException;

import java.util.List;

/**
 * Service to send mails to the user.
 *
 * @author Noah Peeters
 * @author Hans Rißer
 */
public interface MailService {
    /**
     * Sends a mail with the registration token.
     *
     * @param token    The registration token.
     * @param fullName The name of the user.
     * @param email    The email of the user.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
    void sendRegistrationMail(String token, String fullName, String email) throws MailClientException;

    /**
     * Send a mail to a user, when there is already an account with the given email.
     *
     * @param fullName The name of the user.
     * @param email    The email of the user.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
    void sendRegistrationMailDuplicateEmail(String fullName, String email) throws MailClientException;

    /**
     * Sends a mail.
     *
     * @param subject The subject of the mail.
     * @param body    The body of the mail.
     * @param email   The email the mail will be send to.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
    void sendMail(String subject, String body, String email) throws MailClientException;

    /**
     * Sends a mail to every user to inform them, that their participation is stale.
     *
     * @param survey       The survey the participations belong to.
     * @param participants The participants who must creat a new response.
     */
    void sendNeedsAttentionMailsAsync(Survey survey, List<User> participants);

    /**
     * Sends a mail to a user to inform them, that their participation is stale.
     *
     * @param survey      The survey the participations belong to.
     * @param participant The user with the stale response.
     * @throws MailClientException Thrown, when the mail cannot be send.
     */
    void sendNeedsAttentionMail(Survey survey, User participant) throws MailClientException;
}
