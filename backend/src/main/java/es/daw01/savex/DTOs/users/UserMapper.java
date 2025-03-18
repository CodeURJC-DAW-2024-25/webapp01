package es.daw01.savex.DTOs.users;

import java.util.List;

import org.mapstruct.Mapper;

import es.daw01.savex.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    PublicUserDTO toPublicUserDTO(User user);
    List<PublicUserDTO> toPublicUserDTOs(List<User> users);
}
