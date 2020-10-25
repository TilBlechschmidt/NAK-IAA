package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static de.nordakademie.iaa.noodle.config.SecurityConstants.HASH_PEPPER;

@Component
public class PasswordService {
    private final PasswordEncoder passwordEncoder;

    public PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String hashPassword(String password) {
        String input = getPasswordEncoderInput(password);
        return passwordEncoder.encode(input);
    }

    public boolean isPasswordCorrect(User user, String password) {
        String input = getPasswordEncoderInput(password);
        return passwordEncoder.matches(input, user.getPasswordHash());
    }

    private String getPasswordEncoderInput(String password) {
        return password + HASH_PEPPER;
    }
}
