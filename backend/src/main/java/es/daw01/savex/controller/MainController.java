package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.components.ControllerUtils;

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
    public String getProfilePage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        
        model.addAttribute("title", "SaveX - Perfil");
        return "profile";
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        
        model.addAttribute("title", "SaveX - Administración");
        return "admin";
    }
}
