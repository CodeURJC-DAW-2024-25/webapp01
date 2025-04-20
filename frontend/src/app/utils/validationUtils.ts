export enum ResultCode {
    OK = "The object is valid",
    EMPTY_FIELD = "The field cannot be empty",
    INVALID_EMAIL = "The email is invalid",
    INVALID_USERNAME_FORMAT = "The username can only contain letters and numbers",
    INVALID_USERNAME_LENGTH = "The username must be between 3 and 20 characters",
    INVALID_NAME_FORMAT = "The name can only contain letters, numbers and spaces",
    INVALID_NAME_LENGTH = "The name must be between 3 and 20 characters",
    INVALID_PASSWORD_FORMAT = "The password must contain at least one lowercase letter, one uppercase letter and one number",
    INVALID_PASSWORD_LENGTH = "The password must be between 8 and 50 characters",
    INVALID_PASSWORD_CONFIRMATION = "The password confirmation does not match the password",
    INVALID_NEW_PASSWORD = "The new password cannot be the same as the old password",
    INVALID_PASSWORD = "The old password is incorrect",
    USERNAME_TAKEN = "The username is already taken",
    EMAIL_TAKEN = "The email is already taken",
} //TODO: email taken and username taken checks

export interface CreateUserRequest {
    email: string;
    username: string;
    password: string;
}

export interface ModifyUserRequest {
    email: string;
    username: string;
    name: string;
}

export interface ModifyPasswordRequest {
    oldPassword: string;
    newPassword: string;
    newPasswordConfirmation: string;
}

// Placeholder for hash check functionality.
// Replace with an actual implementation using a crypto library if needed.
function checkPassword(password: string, hashedPassword: string): boolean {
    // For now, this is a stub. In a real application, implement proper hash
    // comparison (e.g., using bcrypt.compare)
    return false;
}

export function isValidUser(request: CreateUserRequest): ResultCode {
    const emailResult = isValidEmail(request.email, true);
    if (emailResult !== ResultCode.OK) return emailResult;

    const usernameResult = isValidUsername(request.username, true);
    if (usernameResult !== ResultCode.OK) return usernameResult;

    const passwordResult = isValidPassword(request.password, true);
    if (passwordResult !== ResultCode.OK) return passwordResult;

    return ResultCode.OK;
}

export function isValidUserModify(request: ModifyUserRequest): ResultCode {
    const emailResult = isValidEmail(request.email, false);
    if (emailResult !== ResultCode.OK) return emailResult;

    const usernameResult = isValidUsername(request.username, false);
    if (usernameResult !== ResultCode.OK) return usernameResult;

    const nameResult = isValidName(request.name, false);
    if (nameResult !== ResultCode.OK) return nameResult;

    return ResultCode.OK;
}

export function validatePasswordUpdate(
    request: ModifyPasswordRequest,
    hashedPassword: string
): ResultCode {
    const newPasswordResult = isValidPassword(request.newPassword, true);
    if (newPasswordResult !== ResultCode.OK) return newPasswordResult;

    if (request.newPassword !== request.newPasswordConfirmation) {
        return ResultCode.INVALID_PASSWORD_CONFIRMATION;
    }

    if (checkPassword(request.newPassword, hashedPassword)) {
        return ResultCode.INVALID_NEW_PASSWORD;
    }

    if (!checkPassword(request.oldPassword, hashedPassword)) {
        return ResultCode.INVALID_PASSWORD;
    }

    return ResultCode.OK;
}

export function isValidEmail(email: string | null, isRequired: boolean): ResultCode {
    if (!isRequired && email === null) {
        return ResultCode.OK;
    }

    if (email === null || email.trim() === "") {
        return ResultCode.EMPTY_FIELD;
    }

    // Regex: one or more allowed characters, followed by "@", domain and TLD.
    const pattern = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z0-9+_.-]+$/;
    if (!pattern.test(email)) {
        return ResultCode.INVALID_EMAIL;
    }

    return ResultCode.OK;
}

export function isValidUsername(username: string | null, isRequired: boolean): ResultCode {
    if (!isRequired && username === null) {
        return ResultCode.OK;
    }

    if (username === null || username.trim() === "") {
        return ResultCode.EMPTY_FIELD;
    }

    if (username.length < 3 || username.length > 20) {
        return ResultCode.INVALID_USERNAME_LENGTH;
    }

    const pattern = /^[a-zA-Z0-9]*$/;
    if (!pattern.test(username)) {
        return ResultCode.INVALID_USERNAME_FORMAT;
    }

    return ResultCode.OK;
}

export function isValidName(name: string | null, isRequired: boolean): ResultCode {
    if (!isRequired && name === null) {
        return ResultCode.OK;
    }

    if (name === null || name.trim() === "") {
        return ResultCode.EMPTY_FIELD;
    }

    if (name.length < 3 || name.length > 20) {
        return ResultCode.INVALID_NAME_LENGTH;
    }

    const pattern = /^[a-zA-Z0-9 ]*$/;
    if (!pattern.test(name)) {
        return ResultCode.INVALID_NAME_FORMAT;
    }

    return ResultCode.OK;
}

export function isValidPassword(password: string | null, isRequired: boolean): ResultCode {
    if (!isRequired && password === null) {
        return ResultCode.OK;
    }

    if (password === null || password.trim() === "") {
        return ResultCode.EMPTY_FIELD;
    }

    if (password.length < 8 || password.length > 50) {
        return ResultCode.INVALID_PASSWORD_LENGTH;
    }

    // Regex: at least one lowercase letter, one uppercase letter, and one digit.
    const pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).*$/;
    if (!pattern.test(password)) {
        return ResultCode.INVALID_PASSWORD_FORMAT;
    }

    return ResultCode.OK;
}