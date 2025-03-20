package es.daw01.savex.DTOs.users;

public record ModifyUserRequest(
    String username,
    String name,
    String email
) {}
