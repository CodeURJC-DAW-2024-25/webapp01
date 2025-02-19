package es.daw01.savex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Post;
import es.daw01.savex.service.PostService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private PostService postService;
    
    @GetMapping("/")
    public String getRootPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        List<Post> posts = postService.findAll();

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
    public String getProfilePage(Model model, HttpServletRequest request) {
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
