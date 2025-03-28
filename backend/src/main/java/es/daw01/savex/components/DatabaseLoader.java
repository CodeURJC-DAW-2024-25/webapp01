package es.daw01.savex.components;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
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
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    private void init() {
        try {
            this.initUsers();
            this.initPosts();
            this.initComments();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading default data into the database.");
        }
    }

    /**
     * Load default users into the database
    */
    private void initUsers() {
        // Load default user
        for (int i = 1; i <= 10; i++) {
            String username = "userDefault" + i;
            String email = "userEmail" + i + "@gmail.com";
            if (
            !userRepository.existsByUsername(username) &&
            !userRepository.existsByEmail(email)
            ) {
            userRepository.save(
                new User(
                email,
                username,
                "User" + i,
                passwordEncoder.encode("pass" + i),
                null,
                UserType.USER
                )
            );
            }
        }

        // Load default admin
        if (
            !userRepository.existsByUsername("admin") &&
            !userRepository.existsByEmail("adminEmail@gmail.com")
        ) {
            userRepository.save(
                new User(
                    "adminEmail@gmail.com",
                    "admin",
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
    private void initPosts() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = new Resource[0];

        resources = resolver.getResources("classpath:/posts/*.md");

        // Load default posts by reading the markdown files
        for (Resource resource : resources) {
            String markdown = null;
            Map<String, List<String>> yamlFrontMatter = null;
            String markdownContent = null;
    
            try {
                markdown = resource.getContentAsString(Charset.defaultCharset());
                yamlFrontMatter = markdownService.extractYamlFrontMatter(markdown);
                markdownContent = markdownService.extractMarkdownContent(markdown);
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
                    markdownContent,
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
        Random random = new Random();
        
        for (Post post : posts) {
            // Retrieve all users
            List<User> users = userRepository.findAll().stream()
            .filter(user -> user.getRole() != UserType.ADMIN) // Exclude admin users
            .collect(Collectors.toList());;

            Collections.shuffle(users); // Mix user list (avoid always the same users commenting)

            int commentsToAdd = Math.min(5, users.size()); // Max 5 comments per post, less if there are fewer users
            for (int i = 0; i < commentsToAdd; i++) {    
                User user = users.get(i); 
               
                if (post.hasCommented(user)) continue;

                if (post.getComments().size() >= commentsToAdd) break;

                String randomComment = commentsData[random.nextInt(commentsData.length)];

                Comment comment = new Comment(user, post, randomComment);
                post.addComment(comment);
                user.addComment(comment);

                commentRepository.save(comment);
            }
            postRepository.save(post);
        }

        DatabaseLoader.logger.info("Comments added to posts.");
    }
}
 