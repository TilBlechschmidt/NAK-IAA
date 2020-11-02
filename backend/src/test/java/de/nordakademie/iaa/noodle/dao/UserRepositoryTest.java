package de.nordakademie.iaa.noodle.dao;

import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.model.UserTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    private User user;

    // CHECK data.sql for the TestData referenced here.

    @Test
    public void findByEmailTest() {
        user = userRepository.findByEmail("Name-1@examle.com");
        assertEquals(-1, user.getId());
    }

    @Test
    public void findByIdTest() {
        user = userRepository.findById(0L);
        assertEquals("Name-0", user.getFullName());

    }

    @Test
    public void saveTest() {
        user = UserTest.testUser();
        assertNull(user.getId());
        userRepository.save(user);
        Long id = user.getId();
        assertNotNull(id);
        assertEquals(entityManager.find(User.class, id), user);
    }

    @Test
    public void deleteTest() {
        user = entityManager.find(User.class,1L);
        assertNotNull(user);
        userRepository.delete(user);
        assertNull(entityManager.find(User.class,1L));
        entityManager.persist(user);
    }
}