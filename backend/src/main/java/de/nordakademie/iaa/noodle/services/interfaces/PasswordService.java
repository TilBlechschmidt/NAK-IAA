package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;

/**
 * Service to manage passwords.
 *
 * @author Noah Peeters
 */
public interface PasswordService {
    /**
     * Creates a hash for the given password.
     *
     * @param password The password to hash.
     * @return A hash (including the salt) for the password.
     * @throws PasswordException Thrown, when the password is invalid,
     *                           because it does not comply with the password rules.
     */
    String hashPassword(String password) throws PasswordException;

    /**
     * Checks if the given password is correct for the user.
     *
     * @param user     The user to check the password for.
     * @param password The password to check.
     * @return <code>True</code> if the password is correct. <code>False</code> otherwise.
     */
    boolean isPasswordCorrect(User user, String password);
}
