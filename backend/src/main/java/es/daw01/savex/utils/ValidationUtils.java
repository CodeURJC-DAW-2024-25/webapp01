package es.daw01.savex.utils;

public class ValidationUtils {

    public enum ResultCode {
        OK,
        INVALID_EMAIL,
        INVALID_USERNAME_FORMAT,
        INVALID_USERNAME_LENGTH,
        INVALID_NAME_FORMAT,
        INVALID_NAME_LENGTH,
        INVALID_PASSWORD_FORMAT,
        INVALID_PASSWORD_LENGTH
    }

    
    private ValidationUtils() {
        /* Prevent instantiation */
    }

    /**
     * Check if a string is a valid email
     * 
     * @param email The email to check
     * @return ResultCode.OK if the email is valid, ResultCode.INVALID_EMAIL otherwise
     */
    public static ResultCode isValidEmail(String email) {
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
        if (password.length() < 8 || password.length() > 50) {
            return ResultCode.INVALID_PASSWORD_LENGTH;
        }
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$")) {
            return ResultCode.INVALID_PASSWORD_FORMAT;
        }
        return ResultCode.OK;
    }



}
