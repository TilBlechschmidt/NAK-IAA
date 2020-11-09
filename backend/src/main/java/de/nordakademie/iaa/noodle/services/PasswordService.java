package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.PasswordException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final String hashPepper;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(@Value("${spring.noodle.security.hashPepper}") String hashPepper) {
        this.hashPepper = hashPepper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String hashPassword(String password) throws PasswordException {
        if (!isPasswordValid(password)) {
            throw new PasswordException("passwordDoesNotMatchRules");
        }
        String input = getPasswordEncoderInput(password);
        return passwordEncoder.encode(input);
    }

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
