package es.daw01.savex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String getRootPage(Model model) {
        model.addAttribute("title", "SaveX - Ahorra dinero, tiempo y esfuerzo");
        return "index";
    }
    
    @GetMapping("/about")
    public String getAboutPage(Model model) {
        model.addAttribute("title", "SaveX - Sobre nosotros");
        return "about";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {

        model.addAttribute("title", "SaveX - Perfil");
        return "profile";
    }

    @GetMapping("/admin")
    public String getAdminPage(Model model) {
        model.addAttribute("title", "SaveX - Administración");
        return "admin";
    }
}
