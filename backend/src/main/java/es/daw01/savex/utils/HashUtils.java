package es.daw01.savex.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HashUtils {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private HashUtils() {
        /* Prevent instantiation */
    }

    /**
     * Hash a password using the BCrypt algorithm
     *
     * @param password The password to hash
     * @return The hashed password
     */
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Check if a password matches a hashed password
     *
     * @param password The password to check
     * @param hashedPassword The hashed password to check against
     * @return True if the password matches the hashed password, false otherwise
     */
    public static boolean checkPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
