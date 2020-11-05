package de.nordakademie.iaa.noodle.services;

import de.nordakademie.iaa.noodle.dao.UserRepository;
import de.nordakademie.iaa.noodle.model.Survey;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.exceptions.ConflictException;
import de.nordakademie.iaa.noodle.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void getUserByEMailNotFoundTest() {
        when(userRepository.findByEmail("EMAIL")).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> userService.getUserByEMail("EMAIL"));

        assertEquals("userNotFound", exception.getMessage());
    }

    @Test
    void getUserByEMailTest() throws EntityNotFoundException {
        User user = mock(User.class);
        when(userRepository.findByEmail("EMAIL")).thenReturn(user);

        User fetchedUser = userService.getUserByEMail("EMAIL");
        assertEquals(user, fetchedUser);
    }

    @Test
    void existsUserWithEMailNotFoundTest() {
        when(userRepository.findByEmail("EMAIL")).thenReturn(null);
        boolean exists = userService.existsUserWithEMail("EMAIL");
        assertFalse(exists);
    }

    @Test
    void existsUserWithEMailFoundTest() {
        User user = mock(User.class);
        when(userRepository.findByEmail("EMAIL")).thenReturn(user);
        boolean exists = userService.existsUserWithEMail("EMAIL");
        assertTrue(exists);
    }

    @Test
    void getUserByUserIDNotFoundTest() {
        when(userRepository.findById(42L)).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
            () -> userService.getUserByUserID(42L));

        assertEquals("userNotFound", exception.getMessage());
    }

    @Test
    void getUserByUserIDTest() throws EntityNotFoundException {
        User user = mock(User.class);
        when(userRepository.findById(42L)).thenReturn(user);

        User fetchedUser = userService.getUserByUserID(42L);
        assertEquals(user, fetchedUser);
    }

    @Test
    void createNewUserDuplicateEmailTest() {
        when(userRepository.save(any())).thenThrow(mock(DataIntegrityViolationException.class));

        ConflictException exception = assertThrows(ConflictException.class,
            () -> userService.createNewUser("EMAIL", "FULL_NAME", "PASSWORD_HASH"));

        assertEquals("emailDuplicate", exception.getMessage());
    }

    @Test
    void createNewUserTest() throws ConflictException {
        User user = userService.createNewUser("EMAIL", "FULL_NAME", "PASSWORD_HASH");

        verify(userRepository).save(argThat(savedUser -> {
            assertEquals(user, savedUser);
            return true;
        }));

        assertEquals("EMAIL", user.getEmail());
        assertEquals("FULL_NAME", user.getFullName());
        assertEquals("PASSWORD_HASH", user.getPasswordHash());
        assertEquals(0, user.getCreatedSurveys().size());
        assertEquals(0, user.getParticipations().size());
    }
}