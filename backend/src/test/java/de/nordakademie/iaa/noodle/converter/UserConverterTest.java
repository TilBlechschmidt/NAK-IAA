package de.nordakademie.iaa.noodle.converter;

import de.nordakademie.iaa.noodle.api.model.IdentifiableUserDTO;
import de.nordakademie.iaa.noodle.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserConverterTest {
    private UserConverter userConverter;

    @BeforeEach
    public void setUp() {
        userConverter = new UserConverter();
    }

    @Test
    public void testConvertUserToDTO() {
        User user = mock(User.class);

        when(user.getId()).thenReturn(42L);
        when(user.getFullName()).thenReturn("FULL_NAME");

        IdentifiableUserDTO identifiableUserDTO = userConverter.convertUserToDTO(user);

        assertEquals(42L, identifiableUserDTO.getId());
        assertEquals("FULL_NAME", identifiableUserDTO.getName());
    }
}
