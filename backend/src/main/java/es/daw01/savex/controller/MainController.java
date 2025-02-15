package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.components.ControllerUtils;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Autowired
    private ControllerUtils controllerUtils;
    
    @GetMapping("/")
    public String getRootPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        model.addAttribute("title", "SaveX - Ahorra dinero, tiempo y esfuerzo");
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
        // Get user CSRF token from login form
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");

        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        
        model.addAttribute("token", token.getToken());
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
