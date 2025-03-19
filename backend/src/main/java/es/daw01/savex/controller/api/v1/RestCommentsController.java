package es.daw01.savex.controller.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.comments.CreateCommentRequest;
import es.daw01.savex.DTOs.comments.SimpleCommentDTO;
import es.daw01.savex.DTOs.posts.PostDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.User;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.PostService;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/posts/{id}/comments")
public class RestCommentsController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping({ "", "/" })
    public ResponseEntity<Object> getComments(
            @PathVariable Long id,
            @PageableDefault(page = 0, size = 5) Pageable pageable) {
        PostDTO post = postService.getPost(id);
        if (post.visibility().equals(VisibilityType.PRIVATE) & !controllerUtils.isAuthenticatedUserAdmin()) {
            return ApiResponseDTO.error("Post is private", 403);
        }

        PaginatedDTO<SimpleCommentDTO> response = commentService.retrieveComments(id, pageable);
        return ApiResponseDTO.ok(response);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Object> createComment(
            @PathVariable Long id,
            @ModelAttribute CreateCommentRequest request) {
        User author = controllerUtils.getAuthenticatedUser();
        SimpleCommentDTO comment = commentService.createComment(id, request, author);

        return ApiResponseDTO.ok(comment, 201);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Object> getComment(
            @PathVariable Long id,
            @PathVariable Long commentId) {
        SimpleCommentDTO comment = commentService.getComment(id, commentId);
        return ApiResponseDTO.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable Long id,
            @PathVariable Long commentId) {
        User author = controllerUtils.getAuthenticatedUser();
        try {
            SimpleCommentDTO comment = commentService.deleteComment(id, commentId, author);
            return ApiResponseDTO.ok(comment);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to delete comment (check if the comment exists)");
        }
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<Object> updateComment(
            @PathVariable Long id,
            @PathVariable Long commentId,
            @ModelAttribute CreateCommentRequest request) {
        User author = controllerUtils.getAuthenticatedUser();
        try {
            SimpleCommentDTO updatedComment = commentService.updateComment(id, commentId, request, author);
            return ApiResponseDTO.ok(updatedComment);
        } catch (ResponseStatusException e) {
            return ApiResponseDTO.error("Failed to update comment");
        }
    }
}
