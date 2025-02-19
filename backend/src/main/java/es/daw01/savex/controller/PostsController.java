package es.daw01.savex.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.service.MarkdownService;
import es.daw01.savex.service.PostService;


@Controller
public class PostsController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private MarkdownService markdownService;

    @Autowired
    private PostService postService;
    
    @GetMapping("/posts")
    public String getPostsPage(Model model) {
        List<Post> posts = postService.findAll();

        // Remove private posts
        posts.removeIf(post -> post.getVisibility() == VisibilityType.PRIVATE);

        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        // Add template variables and render the view
        model.addAttribute("title", "SaveX - Aprende más con nuestras guías y posts");
        model.addAttribute("extendedClass", "extended");
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/posts/{id}")
    public String getPostPage(Model model, @PathVariable long id) throws SQLException {
        // Get the post by id
        Optional<Post> op = postService.findById(id);

        if (op.isEmpty()) throw new IllegalArgumentException("Post not found");
        
        Post post = op.get();

        // If the post is private, redirect to the posts page
        if (post.getVisibility() == VisibilityType.PRIVATE) {
            return "redirect:/posts";
        }

        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        // Add template variables and render the view
        model.addAttribute("title", "SaveX - " + post.getTitle());
        model.addAttribute("post", post);
        model.addAttribute("content", markdownService.renderMarkdown(post.getContent()));
        return "post";
    }

    @GetMapping("/posts/{id}/banner")
    public ResponseEntity<Object> getPostBanner(@PathVariable long id) throws SQLException {
        Blob banner = null;
        
        Optional<Post> op = postService.findById(id);

        // If the post does not exist or the banner is null, return a 404
        if (!op.isPresent() || op.get().getBanner() == null) {
            Resource img = new ClassPathResource("static/assets/template_image.png");
            try {
                banner = BlobProxy.generateProxy(img.getInputStream(), img.contentLength());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                .contentLength(banner.length())
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(new InputStreamResource(banner.getBinaryStream()));
        }

        // Get the post banner if it exists and return it
        Post post = op.get();
        banner = post.getBanner();
        Resource resource = new InputStreamResource(banner.getBinaryStream());

        return ResponseEntity.ok()
            .contentLength(banner.length())
            .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
            .body(resource);
    }
}
