package es.daw01.savex.controller.api.v1;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

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
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/v1/posts")
public class RestPostsController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @GetMapping({ "", "/" })
    public ResponseEntity<Object> getPosts(
        @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        PaginatedDTO<PostDTO> response = postService.retrievePosts(pageable);
        return ApiResponseDTO.ok(response);
    }
    
    @PostMapping({ "", "/" })
    public ResponseEntity<Object> createPost(
        @ModelAttribute CreatePostRequest createPostRequest,
        @RequestParam(required = false) MultipartFile banner
    ) {
        try {
            PostDTO post = postService.createPost(createPostRequest, banner);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(post.id()).toUri();

            return ApiResponseDTO.ok(location, 201);
        } catch (IOException e) {
            return ApiResponseDTO.error("Failed to create post");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPost(@PathVariable long id) {
        PostDTO post = postService.getPost(id);
        return ApiResponseDTO.ok(post);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updatePost(
        @PathVariable long id,
        @ModelAttribute CreatePostRequest createPostRequest
    ) {
        try {
            PostDTO post = postService.updatePost(id, createPostRequest);
            return ApiResponseDTO.ok(post);
        } catch (IOException e) {
            return ApiResponseDTO.error("Failed to update post");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable long id) {
        try{
            commentService.deleteByPostId(id);
            PostDTO post = postService.deleteById(id);

            return ApiResponseDTO.ok(post);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to delete post");
        }
    }
    
    @GetMapping("/{id}/banner")
    public ResponseEntity<Object> getPostBanner(@PathVariable long id) throws SQLException, IOException {
        Resource banner = postService.getPostBanner(id);

        return ApiResponseDTO.ok(banner);
    }

    @PostMapping("/{id}/banner")
    public ResponseEntity<Object> updatePostBanner(
        @PathVariable long id,
        @RequestParam MultipartFile banner
    ) {
        try {
            PostDTO post = postService.updatePostBanner(id, banner);
            return ApiResponseDTO.ok(post, 201);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to update post banner, maybe the post does not exist", 404);
        }
    }

    @DeleteMapping("/{id}/banner")
    public ResponseEntity<Object> deletePostBanner(@PathVariable long id) {
        try {
            PostDTO post = postService.deletePostBanner(id);
            return ApiResponseDTO.ok(post);
        } catch (Exception e) {
            return ApiResponseDTO.error("Failed to delete post banner");
        }
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<Object> getPostContent(@PathVariable long id) {
        String content = postService.getPostContent(id);
        return ApiResponseDTO.ok(content);
    }
}