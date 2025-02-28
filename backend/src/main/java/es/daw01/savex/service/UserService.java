package es.daw01.savex.service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
     * 
     * @param id The id of the user to delete
     */
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    /**
     * Finds a user by its id
     * 
     * @param id The id of the user to find
     * @return The user with the given id, or null if it doesn't exist
     */
    public User findById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Finds a user by its username
     * 
     * @param username The username of the user to find
     * @return The user with the given username, or null if it doesn't exist
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Saves a user to the database
     * 
     * @param user The user to save
     * @return The saved user
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    public User registerNewUser(UserDTO userDTO) throws EntityExistsException {
        // Handle if the user already exists
        if (usernameExists(userDTO.getUsername()))
            throw new EntityExistsException("Username already exists");
        if (emailExists(userDTO.getEmail()))
            throw new EntityExistsException("Email already exists");

        // Create a new user object
        User user = new User(
                userDTO.getEmail(),
                userDTO.getUsername(),
                userDTO.getUsername(),
                hashPassword(userDTO.getPassword()),
                null,
                UserType.USER);

        // Save the user to the database
        return userRepository.save(user);
    }

    public void updateUserAvatar(String username, MultipartFile avatar) throws IOException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        User user = optionalUser.get();
        user.setAvatar(BlobProxy.generateProxy(avatar.getInputStream(), avatar.getSize()));
        userRepository.save(user);

    }

    public void updateUserAccount(User user, UserDTO userDTO, Map<String, String> errors) {
        // Check if the username exists
        if (usernameExists(userDTO.getUsername()) && !userDTO.getUsername().equals(user.getUsername()))
            errors.put("username", String.format("El nombre de usuario %s ya existe", userDTO.getUsername()));

        // Check if the email exists
        if (emailExists(userDTO.getEmail()) && !userDTO.getEmail().equals(user.getEmail()))
            errors.put("email", String.format("El email %s ya existe", userDTO.getEmail()));

        // Check if there are errors
        if (!errors.isEmpty()) return;

        user.setEmail(userDTO.getEmail().isBlank() ? user.getEmail() : userDTO.getEmail());
        user.setUsername(userDTO.getUsername().isBlank() ? user.getUsername() : userDTO.getUsername());
        user.setName(userDTO.getName().isBlank() ? user.getName() : userDTO.getName());
        // user.setPassword(userDTO.getPassword() != null ? hashPassword(userDTO.getPassword()) : user.getPassword());
        userRepository.save(user);
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
