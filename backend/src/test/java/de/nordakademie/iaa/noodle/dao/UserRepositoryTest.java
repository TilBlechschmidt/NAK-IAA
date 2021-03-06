package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.model.UserTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for {@link UserRepository}
 *
 * @author Hans Rißer
 */
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    private User user;

    // CHECK data.sql for the TestData referenced here.

    @Test
    void testFindByEmail() {
        user = userRepository.findByEmail("barack.obama@example.com");
        assertEquals(1, user.getId());
    }

    @Test
    void testFindById() {
        user = userRepository.findById(0L);
        assertEquals("Donald Trump", user.getFullName());
    }

    @Test
    void testSave() {
        user = UserTest.testUser();
        assertNull(user.getId());
        userRepository.save(user);
        Long id = user.getId();
        assertNotNull(id);
        assertEquals(entityManager.find(User.class, id), user);
    }
}
