package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.User;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository for {@link User}s.
 */
@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = User.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface UserRepository {

    /**
     * Queries a single user by their unique email.
     *
     * @param email The email of the user.
     * @return The user with the given email or null if it does not exist.
     */
    User findByEmail(String email);

    /**
     * Queries a single user by its id.
     *
     * @param id The id of the user.
     * @return The requested user or null if it does not exists.
     */
    User findById(Long id);

    /**
     * Saves a user.
     *
     * @param user The user to save.
     */
    User save(User user);

    /**
     * Deletes a user.
     *
     * @param user The user to delete.
     */
    void delete(User user);
}
