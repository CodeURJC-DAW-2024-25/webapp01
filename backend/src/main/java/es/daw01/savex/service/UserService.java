package es.daw01.savex.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.UserDTO;
import es.daw01.savex.DTOs.users.CreateUserRequest;
import es.daw01.savex.DTOs.users.ModifyUserPassword;
import es.daw01.savex.DTOs.users.ModifyUserRequest;
import es.daw01.savex.DTOs.users.PrivateUserDTO;
import es.daw01.savex.DTOs.users.PublicUserDTO;
import es.daw01.savex.DTOs.users.UserMapper;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.repository.CommentRepository;
import es.daw01.savex.repository.ShoppingListRepository;
import es.daw01.savex.repository.UserRepository;
import es.daw01.savex.utils.HashUtils;
import es.daw01.savex.utils.ImageUtils;
import es.daw01.savex.utils.ValidationUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    // Public Methods --------------------------------------------------------->>

    public void deleteUserAvatar(long id) {
        User user = userRepository.findById(id).orElseThrow();
        if (user.getAvatar() == null) {
            return;
        }
        user.setAvatar(null);
        userRepository.save(user);
    }

    public PublicUserDTO createUserAvatar(long id, MultipartFile avatar) throws IOException {
        
        User user = userRepository.findById(id).orElseThrow();
        
        if (user.getAvatar() != null) {
            throw new EntityExistsException("User already has an avatar");
        }
        user.setAvatar(ImageUtils.multipartFileToBlob(avatar));
        return userMapper.toPublicUserDTO(userRepository.save(user));
	}

    public PublicUserDTO modifyUserAvatar(long id, MultipartFile avatar) throws IOException {

        User user = userRepository.findById(id).orElseThrow();
        if (user.getAvatar() == null) {
            throw new NoSuchElementException("User doesn't have an avatar");
        }
        user.setAvatar(ImageUtils.multipartFileToBlob(avatar));
        return userMapper.toPublicUserDTO(userRepository.save(user));
    }

    public Resource getUserAvatar(long id) throws SQLException {
        User user = userRepository.findById(id).orElseThrow();
        if (user.getAvatar() == null) {
            throw new NoSuchElementException("User doesn't have an avatar");
        }
        return ImageUtils.blobToResource(user.getAvatar());
    }

    /**
     * Finds all users in the database
     * 
     * @return A list of all users
     */
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds all users in the database
     * 
     * @return A list of all users
     */
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<User> findAllByRole(UserType role) {
        return userRepository.findAllByRole(role);
    }

    /**
     * Finds all users in the database filtered by role
     * 
     * @param role The role to filter by
     * @param pageable The pagination information
     * @return A paginated list of public user DTOs
     */
    public PaginatedDTO<PublicUserDTO> findAllByRoleNoPasswd(UserType role, Pageable pageable) {
        Page<User> usersPage = userRepository.findAllByRole(role, pageable);
        List<PublicUserDTO> publicUsers = userMapper.toPublicUserDTOs(usersPage.getContent());

        return new PaginatedDTO<PublicUserDTO>(
            publicUsers,
            usersPage.getNumber(),
            usersPage.getTotalPages(),
            usersPage.getTotalElements(),
            usersPage.getSize(),
            usersPage.isLast()
        );
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
            commentRepository.deleteByAuthorId(id);
            shoppingListRepository.deleteAllByUserId(id);
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
     * Finds a user by its id and returns a public user DTO
     * 
     * @param id The id of the user to find
     * @return The user with the given id, or null if it doesn't exist
     */
    public PublicUserDTO findPublicUserById(Long id) {
        return userMapper.toPublicUserDTO(userRepository.findById(id).orElseThrow());
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
        if (!errors.isEmpty())
            return;

        user.setEmail(userDTO.getEmail().isBlank() ? user.getEmail() : userDTO.getEmail());
        user.setUsername(userDTO.getUsername().isBlank() ? user.getUsername() : userDTO.getUsername());
        user.setName(userDTO.getName().isBlank() ? user.getName() : userDTO.getName());
        // user.setPassword(userDTO.getPassword() != null ? hashPassword(userDTO.getPassword()) : user.getPassword());
        userRepository.save(user);
    }

    public void checkPassword(User user, String password, String newPassword, String ConfirmPassword,
            Map<String, String> errors) {

        //Check if the fields are empty
        if (password.isBlank() || newPassword.isBlank() || ConfirmPassword.isBlank()) {
            errors.put("password", "Todos los campos son obligatorios");
        }

        //Check if the password is correct
        if (!HashUtils.checkPassword(password, user.getHashedPassword())) {
            errors.put("password", "La contraseña actual no coincide");
        }

        //Check if the new password is the same as the current one
        if (!HashUtils.checkPassword(newPassword, user.getHashedPassword())) {
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
            errors.put("newPassword",
                    "La nueva contraseña debe contener al menos una letra mayúscula, una letra minúscula y un número");
        }

        if (!errors.isEmpty())
            return;

        // Update the password
        user.setHashedPassword(HashUtils.hashPassword(newPassword));
        userRepository.save(user);
    }

    public PrivateUserDTO modifyUser(long id, ModifyUserRequest modifyUser){
        User user = userRepository.findById(id).orElseThrow();

        ValidationUtils.ResultCode validationResult = ValidationUtils.isValidUser(modifyUser);
        if (validationResult != ValidationUtils.ResultCode.OK) {
            throw new IllegalArgumentException(validationResult.getErrorMessage());
        }

        userMapper.updateFromModifyUserRequest(modifyUser, user);
        return userMapper.toPrivateUserDTO(userRepository.save(user));
    }

    public PrivateUserDTO modifyPassword(long id, ModifyUserPassword modifyUserPassword, Map<String, String> errors) {
        User user = userRepository.findById(id).orElseThrow();

        checkPassword(
            user, 
            modifyUserPassword.oldPassword(), 
            modifyUserPassword.newPassword(), 
            modifyUserPassword.newPasswordConfirmation(), 
            errors
        );
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return userMapper.toPrivateUserDTO(user);
    }

    public PrivateUserDTO register(CreateUserRequest createUserRequest) throws EntityExistsException, IllegalArgumentException {
        // Handle if the user already exists
        if (usernameExists(createUserRequest.username())) {
            throw new EntityExistsException("Username already exists");
        }

        // Handle if the email already exists
        if (emailExists(createUserRequest.email())) {
            throw new EntityExistsException("Email already exists");
        }

        // Validate new user fields
        ValidationUtils.ResultCode validationResult = ValidationUtils.isValidUser(createUserRequest);
        if (validationResult != ValidationUtils.ResultCode.OK) {
            throw new IllegalArgumentException(validationResult.getErrorMessage());
        }

        // Create a new user object
        User newUser = new User();
        userMapper.createUserFromRequest(createUserRequest, newUser);
    
        // Save the user to the database
        return userMapper.toPrivateUserDTO(userRepository.save(newUser));
    }

    public Map<String, String> validateUserAndReturnErrors(CreateUserRequest createUserRequest) {
        Map<String, String> errors = new HashMap<>();

        // Check if the username exists
        if (usernameExists(createUserRequest.username())) {
            errors.put("username", "Username already exists");
        }

        // Check if the email exists
        if (emailExists(createUserRequest.email())) {
            errors.put("email", "Email already exists");
        }

        // Validate the user email
        ValidationUtils.ResultCode emailResult = ValidationUtils.isValidEmail(
            createUserRequest.email(),
            true
        );

        if (emailResult != ValidationUtils.ResultCode.OK) {
            errors.put("email", emailResult.getErrorMessage());
        }

        // Validate the username
        ValidationUtils.ResultCode usernameResult = ValidationUtils.isValidUsername(
            createUserRequest.username(),
            true
        );

        if (usernameResult != ValidationUtils.ResultCode.OK) {
            errors.put("username", usernameResult.getErrorMessage());
        }

        // Validate the password
        ValidationUtils.ResultCode passwordResult = ValidationUtils.isValidPassword(
            createUserRequest.password(),
            true
        );

        if (passwordResult != ValidationUtils.ResultCode.OK) {
            errors.put("password", passwordResult.getErrorMessage());
        }

        return errors;
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
     * Get the authenticated user
     * @return The authenticated user
    */
    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated and return the user
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            return this.findByUsername(auth.getName()).get();
        }

        return null;
    }
}