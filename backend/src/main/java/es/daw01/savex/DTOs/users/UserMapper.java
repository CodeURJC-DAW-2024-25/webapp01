package es.daw01.savex.DTOs.users;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.utils.HashUtils;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    PublicUserDTO toPublicUserDTO(User user);
    List<PublicUserDTO> toPublicUserDTOs(List<User> users);

    PrivateUserDTO toPrivateUserDTO(User user);
    List<PrivateUserDTO> toPrivateUserDTOs(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", source = "createUserRequest.email")
    @Mapping(target = "name", source = "createUserRequest.username")
    @Mapping(target = "username", source = "createUserRequest.username")
    @Mapping(target = "hashedPassword", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "role", ignore = true)
    void createUserFromRequest(CreateUserRequest createUserRequest, @MappingTarget User user);

    @AfterMapping
    default void afterMapping(CreateUserRequest createUserRequest, @MappingTarget User user) {
        user.setHashedPassword(HashUtils.hashPassword(createUserRequest.password()));
        user.setRole(UserType.USER);
        user.setAvatar(null);
        user.setComments(new ArrayList<>());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashedPassword", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "username", ignore = true)
    void updateFromModifyUserRequest(ModifyUserRequest modifyUserRequest,@MappingTarget User user);
    
    @AfterMapping
    default void afterMapping(ModifyUserRequest modifyUserRequest, @MappingTarget User user) {
        if (modifyUserRequest.name() != null) {
            user.setName(modifyUserRequest.name());
        }
        if (modifyUserRequest.email() != null) {
            user.setEmail(modifyUserRequest.email());
        }
        if (modifyUserRequest.username() != null) {
            user.setUsername(modifyUserRequest.username());
        }
    }
}
