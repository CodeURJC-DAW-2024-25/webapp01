package es.daw01.savex.controller.api.v1;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.posts.CreatePostRequest;
import es.daw01.savex.DTOs.posts.PostDTO;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/v1/posts")
public class RestPostsController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Operation(summary = "Get all posts paginated")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Posts retrieved successfully",
            content = { 
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaginatedDTO.class)
                )
            }
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to retrieve posts",
            content = @Content
                
        )
    })
    @GetMapping({ "", "/" })
    public ResponseEntity<Object> getPosts(
        @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        try{
            PaginatedDTO<PostDTO> response = postService.retrievePosts(pageable);
            return ApiResponseDTO.ok(response);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to retrieve posts");
        }
    }
    
    @Operation(summary = "Create a post")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Post created successfully",
            content = { 
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostDTO.class)
                )
            }
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to create post",
            content = @Content
        )
    })
    @PostMapping({ "", "/" })
    public ResponseEntity<Object> createPost(
        @ModelAttribute CreatePostRequest createPostRequest,
        @RequestParam(required = false) MultipartFile banner
    ) {
        try {
            PostDTO post = postService.createPost(createPostRequest, banner);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(post.id()).toUri();
            return ApiResponseDTO.ok(post, location, 201);
        } catch (IOException e) {
            return ApiResponseDTO.error("Failed to create post");
        }
    }

    @Operation(summary = "Get a post by id")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Post retrieved successfully",
            content = { 
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostDTO.class)
                )
            }
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Post not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to retrieve post",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getPost(@PathVariable long id) {
        try{
            PostDTO post = postService.getPost(id);
            return ApiResponseDTO.ok(post);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("Post not found", 404);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to retrieve post");
        }
    }

    @Operation(summary = "Update a post by id")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Post updated successfully",
            content = { 
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostDTO.class)
                )
            }
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Post not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to update post",
            content = @Content
        )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Object> updatePost(
        @PathVariable long id,
        @ModelAttribute CreatePostRequest createPostRequest
    ) {
        try {
            PostDTO post = postService.updatePost(id, createPostRequest);
            return ApiResponseDTO.ok(post);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("Post not found", 404);
        } catch (IOException e) {
            return ApiResponseDTO.error("Failed to update post");
        }
    }

    @Operation(summary = "Delete a post by id")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Post deleted successfully",
            content = { 
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostDTO.class)
                )
            }
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Post not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to delete post",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable long id) {
        try{
            commentService.deleteByPostId(id);
            PostDTO post = postService.deleteById(id);
            return ApiResponseDTO.ok(post);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("Post not found", 404);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to delete post");
        }
    }
    
    @Operation(summary = "Get post banner")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Post banner retrieved successfully",
            content = { 
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Resource.class)
                )
            }
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Post not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to get post banner",
            content = @Content
        )
    })
    @GetMapping("/{id}/banner")
    public ResponseEntity<Object> getPostBanner(@PathVariable long id) throws SQLException, IOException {
        try{
            Resource banner = postService.getPostBanner(id);
            return ApiResponseDTO.ok(banner);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("Post not found", 404);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to get post banner");
        }
    }

    @Operation(summary = "Update post banner")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Post banner updated successfully",
            content = { 
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostDTO.class)
                )
            }
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Post not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to update post banner",
            content = @Content
        )
    })
    @PostMapping("/{id}/banner")
    public ResponseEntity<Object> updatePostBanner(
        @PathVariable long id,
        @RequestParam MultipartFile banner
    ) {
        try {
            PostDTO post = postService.updatePostBanner(id, banner);
            return ApiResponseDTO.ok(post, 201);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("Post not found", 404);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to update post banner");
        }
    }

    @Operation(summary = "Delete post banner")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Post banner deleted successfully",
            content = { 
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PostDTO.class)
                )
            }
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Post not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to delete post banner",
            content = @Content
        )
    })
    @DeleteMapping("/{id}/banner")
    public ResponseEntity<Object> deletePostBanner(@PathVariable long id) {
        try {
            PostDTO post = postService.deletePostBanner(id);
            return ApiResponseDTO.ok(post);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("Post not found", 404);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to delete post banner");
        }
    }

    @Operation(summary = "Get post content")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Post content retrieved successfully",
            content = { 
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class)
                )
            }
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Post not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Failed to get post content",
            content = @Content
        )
    })
    @GetMapping("/{id}/content")
    public ResponseEntity<Object> getPostContent(@PathVariable long id) {
        try {
            String content = postService.getPostContent(id);
            return ResponseEntity.ok(content);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("Post not found", 404);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to get post content");
        }
    }
}