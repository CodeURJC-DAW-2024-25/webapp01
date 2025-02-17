package es.daw01.savex.components;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.daw01.savex.model.Post;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.repository.PostRepository;
import es.daw01.savex.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        this.initUsers();
        this.initPosts();
    }

    /**
     * Load default users into the database
    */
    private void initUsers() {
        // Load default user
        if (
            !userRepository.existsByUsername("userDefault") &&
            !userRepository.existsByEmail("userEmail@gmail.com")
        ) {
            userRepository.save(
                new User(
                    "userEmail@gmail.com",
                    "userDefault",
                    "User",
                    passwordEncoder.encode("pass"),
                    null,
                    UserType.USER
                )
            );
        }

        // Load default admin
        if (
            !userRepository.existsByUsername("adminDefault") &&
            !userRepository.existsByEmail("adminEmail@gmail.com")
        ) {
            userRepository.save(
                new User(
                    "adminEmail@gmail.com",
                    "adminDefault",
                    "Admin",
                    passwordEncoder.encode("admin"),
                    null,
                    UserType.ADMIN
                )
            );
        }
    }

    /**
     * Load default posts into the database
    */
    private void initPosts() {
        // Load default post
        if (!postRepository.existsByTitle("Default Post")) {
            postRepository.save(
                new Post(
                    "Default Post",
                    "This is a default post description",
                    "<h1>Hello Mustache from HTML</h1>",
                    "saveX",
                    "2025-02-17",
                    VisibilityType.PUBLIC,
                    Arrays.asList("default", "post"),
                    null
                )
            );
        }
    }
}
