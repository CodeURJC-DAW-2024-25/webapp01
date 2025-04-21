export enum ResultCode {
    OK = "El objeto es válido",
    EMPTY_FIELD = "El campo no puede estar vacío",
    INVALID_EMAIL = "El correo electrónico es inválido",
    INVALID_USERNAME_FORMAT = "El nombre de usuario solo puede contener letras y números",
    INVALID_USERNAME_LENGTH = "El nombre de usuario debe tener entre 3 y 20 caracteres",
    INVALID_NAME_FORMAT = "El nombre solo puede contener letras, números y espacios",
    INVALID_NAME_LENGTH = "El nombre debe tener entre 3 y 20 caracteres",
    INVALID_PASSWORD_FORMAT = "La contraseña debe contener al menos una letra minúscula, una letra mayúscula y un número",
    INVALID_PASSWORD_LENGTH = "La contraseña debe tener entre 8 y 50 caracteres",
    INVALID_PASSWORD_CONFIRMATION = "La confirmación de la contraseña no coincide con la contraseña",
}

import { RegisterUser, ModifyUser, UserPassword } from "@/types/User";

export function isValidUser(request: RegisterUser): RegisterUser {
    
    const errors: RegisterUser = {
        email: "",
        username: "",
        password: "",
    };
    const emailResult = isValidEmail(request.email, true);
    if (emailResult !== ResultCode.OK) {
        errors.email = emailResult;
    }

    const usernameResult = isValidUsername(request.username, true);
    if (usernameResult !== ResultCode.OK) {
        errors.username = usernameResult;
    }

    const passwordResult = isValidPassword(request.password, true);
    if (passwordResult !== ResultCode.OK) {
        errors.password = passwordResult;
    }
    
    return errors;
}

export function isValidUserModify(request: ModifyUser): ModifyUser {

    const errors: ModifyUser = {
        name: "",
        username: "",
        email: "",
    };
    const emailResult = isValidEmail(request.email, false);
    if (emailResult !== ResultCode.OK) {
        errors.email = emailResult;
    }

    const usernameResult = isValidUsername(request.username, false);
    if (usernameResult !== ResultCode.OK) {
        errors.username = usernameResult;
    }

    const nameResult = isValidName(request.name, false);
    if (nameResult !== ResultCode.OK) {
        errors.name = nameResult;
    }

    return errors;
}

export function validatePasswordUpdate(
    request: UserPassword,
): UserPassword {
    const errors: UserPassword = {
        oldPassword: "",
        newPassword: "",
        newPasswordConfirmation: "",
    };

    const newPasswordResult = isValidPassword(request.newPassword, true);
    if (newPasswordResult !== ResultCode.OK) {
        errors.newPassword = newPasswordResult;
    }

    const newPasswordConfirmationResult = isValidPassword(request.newPasswordConfirmation, true,);
    if (newPasswordConfirmationResult !== ResultCode.OK) {
        errors.newPasswordConfirmation = newPasswordConfirmationResult;
    }

   
    if (request.newPassword !== request.newPasswordConfirmation) {
        errors.newPasswordConfirmation = ResultCode.INVALID_PASSWORD_CONFIRMATION;
    }

    return errors;
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