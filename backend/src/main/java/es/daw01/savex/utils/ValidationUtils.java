package es.daw01.savex.utils;

import java.util.List;

import es.daw01.savex.model.UserDTO;

import java.util.ArrayList;

public class ValidationUtils {

    public static List<String> validateDTO(UserDTO userDTO){
        
        List<String> errors = new ArrayList<>();

        if (!isEmailValid(userDTO.getEmail())) {
            errors.add("invalid email format");
        }

        if (!isPasswordValid(userDTO.getPassword())) {
            errors.add("the password must be at least 8 char");
        }

        if (!isUsernameValid(userDTO.getUsername())) {
            errors.add("the username is too short");
        }

        return errors;
    }

    public static String encodeErrors(List<String> errors) {
        StringBuilder encodedQuery = new StringBuilder();
        for (String error : errors) {
            if (encodedQuery.length() > 0) {
                encodedQuery.append("&");
            }
            encodedQuery.append("error=").append(error.replace(" ", "%20"));
        }
        return encodedQuery.toString();
    }

    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    public static boolean isUsernameValid(String username) {
        return username.length() >= 4;
    }
}
