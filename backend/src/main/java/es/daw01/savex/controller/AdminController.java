package es.daw01.savex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.daw01.savex.DTOs.PostDTO;
import es.daw01.savex.DTOs.UserDTO;
import es.daw01.savex.model.UserType;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.PostService;
import es.daw01.savex.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // --- Services ---
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ControllerUtils controllerUtils;

    // --- Methods ---
    @GetMapping("")
    public String getAdminPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        List<PostDTO> postDetails = postService.getPostsDTO(postService.findAll());
        List<UserDTO> userDetails = userService.getUsersDTO(userService.findAllByRole(UserType.USER));

        // Add data to the model
        model.addAttribute("title", "SaveX - Panel de administración");
        model.addAttribute("posts", postDetails);
        model.addAttribute("users", userDetails);

        // model.addAttribute("comments", commentService.findAll());

        return "admin";
    }

    // @DeleteMapping("/user/{id}")
    // public String deleteUser(Model model, @PathVariable long id) {

    //     User authenticatedUser = controllerUtils.getAuthenticatedUser();
    //     if (authenticatedUser.getId() == id) {
    //         return "redirect:/admin";
    //     }
    //     // Delete comments
    //     commentService.deleteByAuthorId(id);
    //     // Delete user
    //     userService.deleteById(id);

    //     return "redirect:/admin";
    // }

    // @GetMapping("/template/users")
    // public String getUsersTemplateString(Model model, @RequestParam(defaultValue = "5") int max) {
    //     List<UserDTO> users = userService.getUsersDTO(userService.findAllByRole(UserType.USER));

    //     // Slice the list to show only 5 users
    //     model.addAttribute("n", max);
    //     model.addAttribute("total", users.size());
    //     model.addAttribute("maxReached", max >= users.size());
    //     users = users.subList(0, DataUtils.clamp(max, 0, users.size()));
    //     model.addAttribute("users", users);

    //     return "admin/admin-user-card";
    // }

    // @GetMapping("/template/posts")
    // public String getPostsTemplateString(Model model, @RequestParam(defaultValue = "5") int max) {
    //     List<PostDTO> posts = postService.getPostsDTO(postService.findAll());

    //     // Slice the list to show only 5 posts
    //     model.addAttribute("n", max);
    //     model.addAttribute("total", posts.size());
    //     model.addAttribute("maxReached", max >= posts.size());
    //     posts = posts.subList(0, DataUtils.clamp(max, 0, posts.size()));
    //     model.addAttribute("posts", posts);

    //     return "admin/admin-post-card";
    // }

}
