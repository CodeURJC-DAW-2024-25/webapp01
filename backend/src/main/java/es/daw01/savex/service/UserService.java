package es.daw01.savex.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import es.daw01.savex.model.User;
import es.daw01.savex.repository.UserRepository;

public class UserService {
    
    @Autowired
    private UserRepository userRepository;

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
}
