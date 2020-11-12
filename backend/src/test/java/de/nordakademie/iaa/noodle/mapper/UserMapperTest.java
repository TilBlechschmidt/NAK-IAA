package de.nordakademie.iaa.noodle.mapper;

import de.nordakademie.iaa.noodle.api.model.IdentifiableUserDTO;
import de.nordakademie.iaa.noodle.model.User;
import de.nordakademie.iaa.noodle.services.SignUpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link UserMapper}
 *
 * @author Noah Peeters
 */
public class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void testConvertUserToDTO() {
        User user = mock(User.class);

        when(user.getId()).thenReturn(42L);
        when(user.getFullName()).thenReturn("FULL_NAME");

        IdentifiableUserDTO identifiableUserDTO = userMapper.userToIdentifiableUserDTO(user);

        assertEquals(42L, identifiableUserDTO.getId());
        assertEquals("FULL_NAME", identifiableUserDTO.getName());
    }
}
