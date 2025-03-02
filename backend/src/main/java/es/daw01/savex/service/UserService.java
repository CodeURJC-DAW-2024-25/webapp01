package es.daw01.savex.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.daw01.savex.DTOs.UserDTO;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Public Methods --------------------------------------------------------->>

    /**
     * Finds all users in the database
     * 
     * @return A list of all users
     */
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user from the database
     * 
     * @param id The id of the user to delete
     */
    @Transactional
    public void deleteById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            userRepository.delete(user);
        } else {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
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

    public void checkPassword(User user, String password, String newPassword, String ConfirmPassword, Map<String, String> errors) {

        //Check if the fields are empty
        if (password.isBlank() || newPassword.isBlank() || ConfirmPassword.isBlank()) {
            errors.put("password", "Todos los campos son obligatorios");
        }

        //Check if the password is correct
        if (!checkPasswordMatches(password,user.getHashedPassword())) {
            errors.put("password", "La contraseña actual no coincide");
        }

        //Check if the new password is the same as the current one
        if (checkPasswordMatches(newPassword, user.getHashedPassword())) {
            errors.put("newPassword", "La nueva contraseña no puede ser igual a la actual");
        }

        //Check if the new password and the confirm password are the same
        if (!newPassword.equals(ConfirmPassword)) {
            errors.put("confirmPassword", "Las contraseñas no coinciden");
        }

        //Check if the new password is valid
        if (newPassword.length() < 8 || newPassword.length() > 50) {
            errors.put("newPassword", "La nueva contraseña debe tener entre 8 y 50 caracteres");
        } else if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$")) {
            errors.put("newPassword", "La nueva contraseña debe contener al menos una letra mayúscula, una letra minúscula y un número");
        }

        if(!errors.isEmpty()) return;
        
        // Update the password
        user.setHashedPassword(hashPassword(newPassword));
        userRepository.save(user);   
    }

    public List<UserDTO> getUsersDTO(Iterable<User> users) {
        List<UserDTO> usersDTO = new ArrayList<>();
        for (User user : users) {
            usersDTO.add(new UserDTO(user));
        }
        return usersDTO;
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


    /**
     * Checks if a raw password matches an encoded password
     * 
     * @param rawPassword
     * @param encodedPassword
     * @return true if the passwords match, false otherwise
     */
    private boolean checkPasswordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
