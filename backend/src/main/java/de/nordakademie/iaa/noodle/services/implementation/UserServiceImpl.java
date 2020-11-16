package de.nordakademie.iaa.noodle.services.implementation;

import de.nordakademie.iaa.noodle.dao.UserRepository;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import de.nordakademie.iaa.noodle.services.exceptions.ServiceException;
import de.nordakademie.iaa.noodle.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to manage {@link User}s.
 *
 * @author Noah Peeters
 * @see UserRepository
 */
@Service("UserService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ServiceException.class})
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    /**
     * Creates a new UserService.
     *
     * @param userRepository The repository for users.
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByEMail(String email) throws EntityNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("userNotFound");
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsUserWithEMail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByUserID(Long userID) throws EntityNotFoundException {
        User user = userRepository.findById(userID);
        if (user == null) {
            throw new EntityNotFoundException("userNotFound");
        }
        return user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User createNewUser(String email, String fullName, String passwordHash) {
        User user = new User(email, fullName, passwordHash);
        userRepository.save(user);
        return user;
    }
}
