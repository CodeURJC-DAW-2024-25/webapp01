package es.daw01.savex.DTOs.users;

public record ModifyUserPassword(
    String oldPassword,
    String newPassword,
    String newPasswordConfirmation
) {}
