package es.daw01.savex.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.User;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.PostService;

@RestController
@RequestMapping("/api")
public class RestPostsController {

    private final static String TEMPLATE_IMAGE_PATH = "static/assets/template_image.png";

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping("/posts/{id}/banner")
    public ResponseEntity<Object> getPostBanner(@PathVariable long id) throws SQLException {
        Blob banner = null;

        Optional<Post> op = postService.findById(id);

        // If the post does not exist or the banner is null, return a 404
        if (!op.isPresent() || op.get().getBanner() == null) {
            Resource img = new ClassPathResource(TEMPLATE_IMAGE_PATH);
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

    @GetMapping("/posts/{id}/comments")
    public ResponseEntity<Map<String, Object>> getComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size) {
        // Retrieve post and return 404 if it does not exist
        Optional<Post> op = postService.findById(id);
        if (op.isEmpty())
            return ResponseEntity.notFound().build();

        Post post = op.get();
        User currentUser = controllerUtils.getAuthenticatedUser();

        // If the post is private return 403
        if (!post.isPublic())
            return ResponseEntity.status(403).build();

        // Retrieve comments of the post paginated
        Map<String, Object> response = commentService.retrieveCommentsFromPost(
                post,
                currentUser,
                PageRequest.of(page, size));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<Map<String, Object>> getPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        // Retrieve posts paginated
        Map<String, Object> response = postService.retrievePosts(
                PageRequest.of(page, size));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable long id) {
        try {
            commentService.deleteByPostId(id);
            postService.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Error al eliminar el post"));
        }

        return ResponseEntity.ok().body(
                Map.of("message", "Post eliminado correctamente"));
    }
}