package es.daw01.savex.controller.api.v1;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
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

import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.PostDTO;
import es.daw01.savex.DTOs.posts.CreatePostRequest;
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
    public ResponseEntity<PaginatedDTO<PostDTO>> getPosts(
        @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        PaginatedDTO<PostDTO> response = postService.retrievePosts(pageable);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping({ "", "/" })
    public ResponseEntity<PostDTO> createPost(
        @ModelAttribute CreatePostRequest createPostRequest,
        @RequestParam(required = false) MultipartFile banner
    ) {
        try {
            PostDTO post = postService.createPost(createPostRequest, banner);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(post.getId()).toUri();

            return ResponseEntity.created(location).body(post);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable long id) {
        PostDTO post = postService.getPost(id);
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
        @PathVariable long id,
        @ModelAttribute CreatePostRequest createPostRequest
    ) {
        try {
            PostDTO post = postService.updatePost(id, createPostRequest);
            return ResponseEntity.ok(post);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable long id) {
        commentService.deleteByPostId(id);
        PostDTO post = postService.deleteById(id);
        return ResponseEntity.ok(post);
    }
    
    @GetMapping("/{id}/banner")
    public ResponseEntity<Object> getPostBanner(@PathVariable long id) throws SQLException, IOException {
        Resource banner = postService.getPostBanner(id);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, "image/png")
            .body(banner);
    }

    @PostMapping("/{id}/banner")
    public ResponseEntity<PostDTO> updatePostBanner(
        @PathVariable long id,
        @RequestParam MultipartFile banner
    ) {
        try {
            PostDTO post = postService.updatePostBanner(id, banner);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
            return ResponseEntity.created(location).body(post);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}/banner")
    public ResponseEntity<PostDTO> deletePostBanner(@PathVariable long id) {
        PostDTO post = postService.deletePostBanner(id);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{id}/content")
    public ResponseEntity<String> getPostContent(@PathVariable long id) {
        String content = postService.getPostContent(id);
        return ResponseEntity.ok(content);
    }
}