package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.User;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RepositoryDefinition(idClass = Long.class, domainClass = User.class)
@Transactional(propagation = Propagation.REQUIRED)
public interface UserRepository {

    User findByEmail(String email);

    User findById(Long id);

    User save(User toSave);

    void delete(User user);
}
