package de.nordakademie.iaa.noodle.converter;

import de.nordakademie.iaa.noodle.api.model.IdentifiableUserDTO;
import de.nordakademie.iaa.noodle.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public IdentifiableUserDTO convertUserToDTO(User user) {
        IdentifiableUserDTO userDTO = new IdentifiableUserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getFullName());
        return userDTO;
    }
}
