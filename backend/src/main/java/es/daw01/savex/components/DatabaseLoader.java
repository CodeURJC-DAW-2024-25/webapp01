package es.daw01.savex.components;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.daw01.savex.model.Comment;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.repository.CommentRepository;
import es.daw01.savex.repository.PostRepository;
import es.daw01.savex.repository.UserRepository;
import es.daw01.savex.service.MarkdownService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Component
public class DatabaseLoader {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Autowired
    private MarkdownService markdownService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    private void init() {
        this.initUsers();
        this.initPosts();
        this.initComments();
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

        DatabaseLoader.logger.info("Users added to the database.");
    }

    /**
     * Load default posts into the database
    */
    private void initPosts() {

        Resource postsDir = new ClassPathResource("posts");

        // Get all markdown files in the posts directory
        List<Resource> resources = new ArrayList<>();
        try {
            resources = Stream.of(postsDir.getFile().listFiles())
                .filter(file -> file.getName().endsWith(".md"))
                .map(file -> new ClassPathResource("posts/" + file.getName()))
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load default posts by reading the markdown files
        for (Resource resource : resources) {
            String markdown = null;
            Map<String, List<String>> yamlFrontMatter = null;
    
            try {
                markdown = resource.getContentAsString(Charset.defaultCharset());
                yamlFrontMatter = markdownService.extractYamlFrontMatter(markdown);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (postRepository.existsByTitle(yamlFrontMatter.get("title").get(0))) {
                DatabaseLoader.logger.info("Post already exists, skipping...");
                continue;
            }

            // Save the post if the markdown content is not empty
            if (markdown != null && !markdown.isEmpty()) {
                postRepository.save(new Post(
                    markdown,
                    yamlFrontMatter
                ));
            }
        }

        DatabaseLoader.logger.info("Posts added to the database.");
    }

    /**
     * Load default comments into the database
    */
    private void initComments() {

        String[] commentsData = {
            "Me ha encantado el post, gracias por compartirlo!",
            "¡Qué interesante! No sabía que...",
            "¡Muy buen post! Me ha sido de gran ayuda.",
            "¡Gracias por la información! Me ha sido muy útil.",
            "Tengo una duda, ¿podrías explicar más sobre el tema en cuestión?",
            "¡Muy buen trabajo! Sigue así.",
            "¡Excelente post! Me ha encantado.",
        };
        
        // Retrieve all posts
        List<Post> posts = postRepository.findAll();

        // Retrieve all users
        List<User> users = userRepository.findAll();

        for (Post post : posts) {
            for (User user : users) {
                // Skip the admin user
                if (user.getRole() == UserType.ADMIN) continue;

                int randomCommentIndex = (int) (Math.random() * commentsData.length);

                // Check if the user has commented already on the post
                if (post.hasCommented(user)) continue;

                // Create a new comment and add it to the post and user
                Comment comment = new Comment(user, post, commentsData[randomCommentIndex]);
                post.addComment(comment);
                user.addComment(comment);

                // Save data to the database
                commentRepository.save(comment);
                postRepository.save(post);
                userRepository.save(user);
            }
        }

        DatabaseLoader.logger.info("Comments added to posts.");
    }
}
