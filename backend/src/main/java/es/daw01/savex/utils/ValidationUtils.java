package es.daw01.savex.utils;

import es.daw01.savex.DTOs.users.CreateUserRequest;

public class ValidationUtils {

    public enum ResultCode {
        OK("The object is valid"),
        EMPTY_FIELD("The field cannot be empty"),
        INVALID_EMAIL("The email is invalid"),
        INVALID_USERNAME_FORMAT("The username can only contain letters and numbers"),
        INVALID_USERNAME_LENGTH("The username must be between 3 and 20 characters"),
        INVALID_NAME_FORMAT("The name can only contain letters, numbers and spaces"),
        INVALID_NAME_LENGTH("The name must be between 3 and 20 characters"),
        INVALID_PASSWORD_FORMAT("The password must contain at least one lowercase letter, one uppercase letter and one number"),
        INVALID_PASSWORD_LENGTH("The password must be between 8 and 50 characters");

        private final String errorMessage;

        ResultCode(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    // Constructors ----------------------------------------------------------->>
    
    private ValidationUtils() {
        /* Prevent instantiation */
    }

    // Public methods --------------------------------------------------------->>

    /**
     * Check if a User object is valid
     * 
     * @param createUserRequest The User object to check
     * @return ResultCode.OK if the User object is valid, the corresponding ResultCode otherwise
     */
    public static ResultCode isValidUser(CreateUserRequest createUserRequest) {
        ResultCode emailResult = isValidEmail(createUserRequest.email());
        if (emailResult != ResultCode.OK) {
            return emailResult;
        }

        ResultCode usernameResult = isValidUsername(createUserRequest.username());
        if (usernameResult != ResultCode.OK) {
            return usernameResult;
        }

        ResultCode passwordResult = isValidPassword(createUserRequest.password());
        if (passwordResult != ResultCode.OK) {
            return passwordResult;
        }

        return ResultCode.OK;
    }

    /**
     * Check if a string is a valid email
     * 
     * @param email The email to check
     * @return ResultCode.OK if the email is valid, ResultCode.INVALID_EMAIL otherwise
     */
    public static ResultCode isValidEmail(String email) {
        if (email.isEmpty()) {
            return ResultCode.EMPTY_FIELD;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9+_.-]")) {
            return ResultCode.INVALID_EMAIL;
        }

        return ResultCode.OK;   
    }

    /**
     * Check if a string is a valid username
     * 
     * @param username The username to check
     * @return ResultCode.OK if the username is valid, ResultCode.INVALID_USERNAME_FORMAT or ResultCode.INVALID_USERNAME_LENGTH otherwise
     */
    public static ResultCode isValidUsername(String username) {
        if (username.isEmpty()) {
            return ResultCode.EMPTY_FIELD;
        }

        if (username.length() < 3 || username.length() > 20) {
            return ResultCode.INVALID_USERNAME_LENGTH;
        }

        if (!username.matches("^[a-zA-Z0-9]*$")) {
            return ResultCode.INVALID_USERNAME_FORMAT;
        }

        return ResultCode.OK;
    }

    /**
     * Check if a string is a valid name
     * 
     * @param name The name to check
     * @return ResultCode.OK if the name is valid, ResultCode.INVALID_NAME_FORMAT or ResultCode.INVALID_NAME_LENGTH otherwise
     */
    public static ResultCode isValidName(String name) {
        if (name.isEmpty()) {
            return ResultCode.EMPTY_FIELD;
        }

        if (name.length() < 3 || name.length() > 20) {
            return ResultCode.INVALID_NAME_LENGTH;
        }

        if (!name.matches("^[a-zA-Z0-9 ]*$")) {
            return ResultCode.INVALID_NAME_FORMAT;
        }

        return ResultCode.OK;
    }

    /**
     * Check if a string is a valid password
     * 
     * @param password The password to check
     * @return ResultCode.OK if the password is valid, ResultCode.INVALID_PASSWORD_FORMAT or ResultCode.INVALID_PASSWORD_LENGTH otherwise
     */
    public static ResultCode isValidPassword(String password) {
        if (password.isEmpty()) {
            return ResultCode.EMPTY_FIELD;
        }

        if (password.length() < 8 || password.length() > 50) {
            return ResultCode.INVALID_PASSWORD_LENGTH;
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$")) {
            return ResultCode.INVALID_PASSWORD_FORMAT;
        }

        return ResultCode.OK;
    }
}