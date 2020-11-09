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

/**
 * Service to manage users.
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class })
public class UserService {
    private final UserRepository userRepository;

    /**
     * Creates a new UserService.
     * @param userRepository The repository for users.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Queries a user identified by the unique email.
     * @param email The email of the user.
     * @return The requested user.
     * @throws EntityNotFoundException Thrown, when the use does not exist.
     */
    public User getUserByEMail(String email) throws EntityNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) { throw new EntityNotFoundException("userNotFound"); }
        return user;
    }

    public boolean existsUserWithEMail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    /**
     * Queries a user wit an id.
     * @param userID The id of the user.
     * @return The requested user.
     * @throws EntityNotFoundException Thrown, when the use does not exist.
     */
    public User getUserByUserID(Long userID) throws EntityNotFoundException {
        User user = userRepository.findById(userID);
        if (user == null) { throw new EntityNotFoundException("userNotFound"); }
        return user;
    }

    /**
     * Creates a new user.
     * @param email The email of the user.
     * @param fullName The full name of the user.
     * @param passwordHash The password hash.
     * @return The new user.
     * @throws ConflictException Thrown, when there is already a user with the given email.
     */
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
