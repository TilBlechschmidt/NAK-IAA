package de.nordakademie.iaa.noodle.services.interfaces;

import de.nordakademie.iaa.noodle.dao.UserRepository;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;

/**
 * Service to manage {@link User}s.
 *
 * @author Noah Peeters
 * @see UserRepository
 */
public interface UserService {
    /**
     * Queries a user identified by the unique email.
     *
     * @param email The email of the user.
     * @return The requested user.
     * @throws EntityNotFoundException Thrown, when the use does not exist.
     */
    User getUserByEMail(String email) throws EntityNotFoundException;

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The requested user's email
     * @return True, if a user with the given email exists.
     */
    boolean existsUserWithEMail(String email);

    /**
     * Queries a user wit an id.
     *
     * @param userID The id of the user.
     * @return The requested user.
     * @throws EntityNotFoundException Thrown, when the use does not exist.
     */
    User getUserByUserID(Long userID) throws EntityNotFoundException;

    /**
     * Creates a new user.
     *
     * @param email        The email of the user.
     * @param fullName     The full name of the user.
     * @param passwordHash The password hash.
     * @return The new user.
     */
    User createNewUser(String email, String fullName, String passwordHash);
}
