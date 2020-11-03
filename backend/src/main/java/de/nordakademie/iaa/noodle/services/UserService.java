package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.UserRepository;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.ConflictException;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class })
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEMail(String email) throws EntityNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) { throw new EntityNotFoundException("userNotFound"); }
        return user;
    }

    public boolean existsUserWithEMail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User getUserByUserID(Long userID) throws EntityNotFoundException {
        User user = userRepository.findById(userID);
        if (user == null) { throw new EntityNotFoundException("userNotFound"); }
        return user;
    }

    public User createNewUser(String email, String fullName, String passwordHash) throws ConflictException {
        try {
            User user = new User(new HashSet<>(), new HashSet<>(), email, fullName, passwordHash);
            userRepository.save(user);
            return user;
        } catch (DataIntegrityViolationException e) {
            // EMail address already exists in the database.
            throw new ConflictException("emailDuplicate");
        }
    }
}
