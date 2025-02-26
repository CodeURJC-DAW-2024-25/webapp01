package es.daw01.savex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Post;
import es.daw01.savex.service.PostService;

@Controller
public class MainController {

    private static final int LANDING_POSTS = 4;

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private PostService postService;
    
    @GetMapping("/")
    public String getRootPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        //Add posts to the model
        List<Post> posts = postService
            .findAllOrderByCreatedAtDesc(PageRequest.of(0, LANDING_POSTS))
            .getContent();

        model.addAttribute("title", "SaveX - Ahorra dinero, tiempo y esfuerzo");
        model.addAttribute("extendedClass", "");
        model.addAttribute("posts", posts);
        return "index";
    }
    
    @GetMapping("/about")
    public String getAboutPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        model.addAttribute("title", "SaveX - Sobre nosotros");
        return "about";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        
        model.addAttribute("title", "SaveX - ".concat(model.getAttribute("name").toString()));
        return "profile";
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        
        model.addAttribute("title", "SaveX - Panel de administración");
        return "admin";
    }
}
