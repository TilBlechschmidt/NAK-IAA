package de.nordakademie.iaa.noodle.model;

import de.nordakademie.iaa.noodle.TestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.HashSet;

import static org.assertj.core.api.Assumptions.assumeThatCode;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public
class UserTest {
    private static Long id;
    private static User user;
    private static int emailCounter = 0;
    @Autowired
    private EntityManager entityManager;

    public static User testUser() {
        return new User(new HashSet<>(), new HashSet<>(), "email" + (emailCounter++), "testName", "testHash");
    }

    @AfterEach
    void tearDown() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testCreate() {
        user = testUser();
        entityManager.persist(user);
        assertNotNull(id = user.getId());
    }

    @Test
    public void testRead() {
        assumeThatCode(this::testCreate).doesNotThrowAnyException();

        user = null;
        user = entityManager.find(User.class, id);

        assertEquals(new HashSet<>(), user.getCreatedSurveys());
        assertEquals(new HashSet<>(), user.getParticipations());
        assertEquals("testName", user.getFullName());
        assertEquals("email" + (emailCounter - 1), user.getEmail());
        assertEquals("testHash", user.getPasswordHash());
    }

    @Test
    public void testUpdate() {
//        We have decided not to make entities updatable before it is necessary
//        This test will be used but skipped until that decision is made final.
        TestUtil.skip();

        assumeThatCode(this::testCreate).doesNotThrowAnyException();
    }

    @Test
    public void testDelete() {
        assumeThatCode(this::testCreate).doesNotThrowAnyException();

        assertNotNull(entityManager.find(User.class, id));
        entityManager.remove(user);
        assertNull(entityManager.find(User.class, id));
    }
}
