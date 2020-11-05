package de.nordakademie.iaa.noodle.mapper;

import de.nordakademie.iaa.noodle.api.model.IdentifiableUserDTO;
import de.nordakademie.iaa.noodle.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface UserMapper {

    @Named("userToIdentifiableUserDTO")
    @Mapping(target = "name", source = "fullName")
    IdentifiableUserDTO userToIdentifiableUserDTO(User user);
}
