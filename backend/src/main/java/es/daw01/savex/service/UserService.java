package es.daw01.savex.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.daw01.savex.model.User;
import es.daw01.savex.model.UserDTO;
import es.daw01.savex.model.UserType;
import es.daw01.savex.repository.UserRepository;
import jakarta.persistence.EntityExistsException;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Deletes a user from the database
     * @param id The id of the user to delete
    */
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    /**
     * Finds a user by its id
     * @param id The id of the user to find
     * @return The user with the given id, or null if it doesn't exist
    */
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Finds a user by its username
     * @param username The username of the user to find
     * @return The user with the given username, or null if it doesn't exist
    */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Saves a user to the database
     * @param user The user to save
     * @return The saved user
    */
    public User save(User user) {
        return userRepository.save(user);
    }

    public User registerNewUser(UserDTO userDTO) throws EntityExistsException {
        // Handle if the user already exists
        if (usernameExists(userDTO.getUsername())) throw new EntityExistsException("Username already exists");
        if (emailExists(userDTO.getEmail())) throw new EntityExistsException("Email already exists");

        // Create a new user object
        User user = new User(
            userDTO.getEmail(),
            userDTO.getUsername(),
            userDTO.getUsername(),
            hashPassword(userDTO.getPassword()),
            null,
            UserType.USER
        );

        // Save the user to the database
        return userRepository.save(user);
    }


    // Private Methods -------------------------------------------------------->>

    /**
     * Checks if a username already exists in the database
     * 
     * @param username
     * @return true if the username exists, false otherwise
    */
    private boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Checks if an email already exists in the database
     * 
     * @param email
     * @return true if the email exists, false otherwise
    */
    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Hashes a password using the password encoder
     * 
     * @param password
     * @return The hashed password
    */
    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
