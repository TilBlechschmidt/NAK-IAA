package de.nordakademie.iaa.noodle;

import de.nordakademie.iaa.noodle.model.User;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.TestAbortedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Utility Class that acts as a namespace for static methods used in Unit Testing.
 *
 * @author Hans Rißer
 * @author Noah Peeters
 */
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
            T.printStackTrace();
            skip();
            return null;
        }
    }

    public static void setupAuthentication() {
        User user = mock(User.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getDetails()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void assertExceptionEquals(HttpStatus expectedStatus, String expectedReason,
                                             ResponseStatusException exception) {
        assertEquals(expectedStatus, exception.getStatus());
        assertEquals(expectedReason, exception.getReason());
    }

    public static void assertThrowsResponseStatusException(HttpStatus expectedStatus, String expectedReason,
                                                           Executable executable) {
        ResponseStatusException exc = assertThrows(ResponseStatusException.class, executable);
        assertExceptionEquals(expectedStatus, expectedReason, exc);
    }

    public static <T> void assertSameResponseEntity(HttpStatus expectedStatus, T expectedBody,
                                                    ResponseEntity<T> responseEntity) {
        assertSame(expectedStatus, responseEntity.getStatusCode());
        assertSame(expectedBody, responseEntity.getBody());
    }
}
