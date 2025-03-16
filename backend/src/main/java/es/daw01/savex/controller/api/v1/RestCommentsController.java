package es.daw01.savex.controller.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.PostDTO;
import es.daw01.savex.DTOs.comments.SimpleCommentDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.PostService;

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
    public ResponseEntity<PaginatedDTO<SimpleCommentDTO>> getComments(
        @PathVariable Long id,
        @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        PostDTO post = postService.getPost(id);
        if (post.getVisibility().equals(VisibilityType.PRIVATE) & !controllerUtils.isAuthenticatedUserAdmin()) {
            return ResponseEntity.badRequest().body(null);
        }

        PaginatedDTO<SimpleCommentDTO> response = commentService.retrieveComments(id, pageable);
        return ResponseEntity.ok(response);
    }
    
}
