package es.daw01.savex.controller.api.v1;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.PostService;
import es.daw01.savex.service.UserService;

@RestController
@RequestMapping("/api/admin")
public class RestAdminController {

    @Autowired
    ControllerUtils controllerUtils;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        try{
            // Get the authenticated user
            User authenticatedUser = controllerUtils.getAuthenticatedUser();

            // Prevent admin from deleting himself
            if (authenticatedUser.getId() == id) {
                return ApiResponseDTO.error("You cannot delete yourself");
            }

            // Before deleting the user, we must delete all the comments and posts associated with him
            // Delete comments
            commentService.deleteByAuthorId(id);
            // Delete user
            userService.deleteById(id);

            // Return success message
            return ApiResponseDTO.ok("User deleted successfully");
        }
        catch (Exception e){
            // Return error message
            return ApiResponseDTO.error("Error deleting user");
        }
    }


    @GetMapping("/users")  // Used to be "/template/users"
    public ResponseEntity<Object> getUsersTemplateString(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        try{
            // Get all users
            Map<String, Object> response = userService.findAllByRole(
                UserType.USER,
                PageRequest.of(page, size)
            );

            // Return the users list
            return ApiResponseDTO.ok(response);
        }
        catch (Exception e){
            // Return error message
            return ApiResponseDTO.error("Error getting users");    
        }
    }


    @GetMapping("/posts")  // Used to be "/template/posts"
    public ResponseEntity<Object> getPostsTemplateString(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        try{
            // Get all posts
            Map<String, Object> response = postService.findAll(
                PageRequest.of(page, size)
            );

            // Return the posts list
            return ApiResponseDTO.ok(response);
        }
        catch (Exception e){
            // Return error message
            return ApiResponseDTO.error("Error getting posts");
        }
    }
} 