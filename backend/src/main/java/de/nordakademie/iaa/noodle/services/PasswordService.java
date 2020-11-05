package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.model.User;
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

    public String hashPassword(String password) {
        String input = getPasswordEncoderInput(password);
        return passwordEncoder.encode(input);
    }

    public boolean isPasswordCorrect(User user, String password) {
        String input = getPasswordEncoderInput(password);
        return passwordEncoder.matches(input, user.getPasswordHash());
    }

    private String getPasswordEncoderInput(String password) {
        return password + hashPepper;
    }
}
