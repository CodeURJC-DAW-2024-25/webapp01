package es.daw01.savex.DTOs.users;

import es.daw01.savex.model.UserType;

public record PublicUserDTO(
    Long id,
    String email,
    String username,
    String name,
    UserType role
) {}
