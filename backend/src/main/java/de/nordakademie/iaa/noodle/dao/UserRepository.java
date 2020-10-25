package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User, Long> {

    User findByEmail(String email);

    User save(User toSave);
}
