package de.nordakademie.iaa.noodle;

import de.nordakademie.iaa.noodle.model.User;
import org.opentest4j.TestAbortedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityManager;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestUtil {
    public static void skip() {
        throw new TestAbortedException();
    }

    public static <T> T requireEntity(Supplier<T> supplier, EntityManager entityManager) {
        return requireEntity(supplier.get(), entityManager);
    }

    public static <T> T requireEntity(Function<EntityManager, T> supplier, EntityManager entityManager) {
        return requireEntity(supplier.apply(entityManager), entityManager);
    }

    private static <T> T requireEntity(T required, EntityManager entityManager) {
        try {
            entityManager.persist(required);
            if (required != null)
                return required;
            skip();
            return null;
        } catch (Throwable T) {
            System.err.println(T);
            skip();
            return null;
        }
    }

    public static User setupAuthentication() {
        User user = mock(User.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getDetails()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return user;
    }
}
