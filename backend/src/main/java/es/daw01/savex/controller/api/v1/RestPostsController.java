package es.daw01.savex.controller.api.v1;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1")
public class RestPostsController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping("/posts")
    public ResponseEntity<Map<String, Object>> getPosts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size
    ) {
        // Retrieve posts paginated
        Map<String, Object> response = postService.retrievePosts(
                PageRequest.of(page, size));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{id}/banner")
    public ResponseEntity<Object> getPostBanner(@PathVariable long id) throws SQLException, IOException {
        Resource banner = postService.getPostBanner(id);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "image/png")
            .body(banner);
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