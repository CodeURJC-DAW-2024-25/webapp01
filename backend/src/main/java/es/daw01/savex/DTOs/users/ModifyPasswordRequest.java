package es.daw01.savex.DTOs.users;

public record ModifyPasswordRequest(
    String oldPassword,
    String newPassword,
    String newPasswordConfirmation
) {}
