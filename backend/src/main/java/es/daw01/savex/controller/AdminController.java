package es.daw01.savex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.daw01.savex.DTOs.UserDTO;
import es.daw01.savex.DTOs.posts.PostDTO;
import es.daw01.savex.model.UserType;
import es.daw01.savex.components.ControllerUtils;
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
}
