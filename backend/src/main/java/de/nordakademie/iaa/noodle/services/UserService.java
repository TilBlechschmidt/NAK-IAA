package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.UserRepository;
import de.nordakademie.iaa.noodle.filter.NoodleException;
import de.nordakademie.iaa.noodle.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;

@Component
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserByEMail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public Optional<User> getUserByUserID(Long userID) {
        return Optional.ofNullable(userRepository.findById(userID));
    }

    public User createNewUser(String email, String fullName, String passwordHash) {
        try {
            User user = new User(new HashSet<>(), new HashSet<>(), email, fullName, passwordHash);
            userRepository.save(user);
            return user;
        } catch (DataIntegrityViolationException e) {
            // EMail address already exists in the database.
            throw NoodleException.conflict("EMail Duplicate");
        }
    }
}
