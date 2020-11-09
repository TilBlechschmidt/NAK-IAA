package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service to manage passwords.
 */
@Service
public class PasswordService {
    private final String hashPepper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new PasswordService.
     * @param hashPepper The pepper used for the password hashing.
     */
    public PasswordService(@Value("${spring.noodle.security.hashPepper}") String hashPepper) {
        this.hashPepper = hashPepper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Creates a hash for the given password.
     * @param password The password to hash.
     * @return A hash (including the salt) for the password.
     * @throws PasswordException Thrown, when the password is invalid,
     * because it does not comply with the password rules.
     */
    public String hashPassword(String password) throws PasswordException {
        if (!isPasswordValid(password)) {
            throw new PasswordException("passwordDoesNotMatchRules");
        }
        String input = getPasswordEncoderInput(password);
        return passwordEncoder.encode(input);
    }

    /**
     * Checks if the given password is correct for the user.
     * @param user The user to check the password for.
     * @param password The password to check.
     * @return <code>True</code> if the password is correct. <code>False</code> otherwise.
     */
    public boolean isPasswordCorrect(User user, String password) {
        if (!isPasswordValid(password)) {
            return false;
        }
        String input = getPasswordEncoderInput(password);
        return passwordEncoder.matches(input, user.getPasswordHash());
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 64;
    }

    private String getPasswordEncoderInput(String password) {
        return password + hashPepper;
    }
}
