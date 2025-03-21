package es.daw01.savex.DTOs.users;

import es.daw01.savex.model.UserType;

public record PrivateUserDTO(
    String username,
    String name,
    String email,
    UserType role,
    long id
){}
