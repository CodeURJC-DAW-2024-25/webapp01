package es.daw01.savex.controller.api.v1;

import java.net.URI;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.comments.CreateCommentRequest;
import es.daw01.savex.DTOs.comments.SimpleCommentDTO;
import es.daw01.savex.DTOs.posts.PostDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/posts/{id}/comments")
public class RestCommentsController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ControllerUtils controllerUtils;

    @Operation(summary = "Get all comments")
    @ApiResponses(value = {
            @ApiResponse(
				responseCode = "200",
				description = "Comments retrieved successfully",
				content = @Content(
					mediaType = "application/json", 
					schema = @Schema(implementation = PaginatedDTO.class)
					)
			),
            @ApiResponse(
				responseCode = "403",
				description = "Post is private",
				content = @Content
			),
            @ApiResponse(
				responseCode = "500",
				description = "Internal server error",
				content = @Content
			)
    })
    @GetMapping({ "", "/" })
    public ResponseEntity<Object> getComments(@PathVariable Long id, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        try {
            PostDTO post = postService.getPost(id);
            if (post.visibility().equals(VisibilityType.PRIVATE) & !controllerUtils.isAuthenticatedUserAdmin()) {
                return ApiResponseDTO.error("Post is private", 403);
            }

            PaginatedDTO<SimpleCommentDTO> response = commentService.retrieveComments(id, pageable);
            return ApiResponseDTO.ok(response);
        } catch (Exception e) {
            return ApiResponseDTO.error("Error getting comments");
        }
    }

    @Operation(summary = "Create a new comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comment created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimpleCommentDTO.class)
                    )
            ),
			@ApiResponse(
					responseCode = "404",
					description = "Post not found",
					content = @Content
			),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PostMapping({ "", "/" })
    public ResponseEntity<Object> createComment(@PathVariable Long id, @RequestBody CreateCommentRequest request) {
        try{
            SimpleCommentDTO comment = commentService.createComment(id, request);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.id()).toUri();
            return ApiResponseDTO.ok(comment, location, 201);
		} catch (NoSuchElementException e) {
			return ApiResponseDTO.error("Post not found", 404);
        } catch (Exception e) {
            return ApiResponseDTO.error("Error creating comment");
        }
    }

    @Operation(summary = "Get a comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimpleCommentDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment not found",
                    content = @Content
            ),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error",
					content = @Content
			)
    })
    @GetMapping("/{commentId}")
    public ResponseEntity<Object> getComment(@PathVariable Long id, @PathVariable Long commentId) {
        try{
            SimpleCommentDTO comment = commentService.getComment(id, commentId);
            return ApiResponseDTO.ok(comment);
		} catch (NoSuchElementException e) {
			return ApiResponseDTO.error("Comment not found", 404);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to get comment");
        }
    }

    @Operation(summary = "Delete a comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimpleCommentDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment not found",
                    content = @Content
            ),
			@ApiResponse(
					responseCode = "500",
					description = "Internal server error",
					content = @Content
			)
    })
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable Long id,
            @PathVariable Long commentId) {
        try {
            SimpleCommentDTO comment = commentService.deleteComment(id, commentId);
            return ApiResponseDTO.ok(comment);
		} catch (NoSuchElementException e) {
			return ApiResponseDTO.error("Comment not found", 404);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to delete comment (check if the comment exists)");
        }
    }

    @Operation(summary = "Update a comment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Comment updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SimpleCommentDTO.class)
                    )
            ),
			@ApiResponse(
					responseCode = "404",
					description = "Comment not found",
					content = @Content
			),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    @PatchMapping("/{commentId}")
    public ResponseEntity<Object> updateComment(
        @PathVariable Long id,
        @PathVariable Long commentId,
        @RequestBody CreateCommentRequest request
    ) {
        try {
            SimpleCommentDTO updatedComment = commentService.updateComment(id, commentId, request);
            return ApiResponseDTO.ok(updatedComment);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("Comment not found", 404);
        } catch (ResponseStatusException e) {
            return ApiResponseDTO.error("Failed to update comment");
        }
    }
}
