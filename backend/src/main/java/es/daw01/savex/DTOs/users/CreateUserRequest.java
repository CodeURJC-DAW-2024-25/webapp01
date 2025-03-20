package es.daw01.savex.DTOs.users;

public record CreateUserRequest(
    String username,
    String password,
    String email
) {}
