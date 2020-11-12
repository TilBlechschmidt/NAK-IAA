package de.nordakademie.iaa.noodle.services.implementation;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import de.nordakademie.iaa.noodle.services.interfaces.PasswordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service to manage passwords.
 *
 * @author Noah Peeters
 */
@Service("PasswordService")
public class PasswordServiceImpl implements PasswordService {
    private final String hashPepper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new PasswordService.
     *
     * @param hashPepper The pepper used for the password hashing.
     */
    public PasswordServiceImpl(@Value("${spring.noodle.security.hashPepper}") String hashPepper) {
        this.hashPepper = hashPepper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String hashPassword(String password) throws PasswordException {
        if (!isPasswordValid(password)) {
            throw new PasswordException("passwordDoesNotMatchRules");
        }
        String input = getPasswordEncoderInput(password);
        return passwordEncoder.encode(input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPasswordCorrect(User user, String password) {
        if (!isPasswordValid(password)) {
            return false;
        }
        String input = getPasswordEncoderInput(password);
        return passwordEncoder.matches(input, user.getPasswordHash());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 64;
    }

    private String getPasswordEncoderInput(String password) {
        return password + hashPepper;
    }
}
