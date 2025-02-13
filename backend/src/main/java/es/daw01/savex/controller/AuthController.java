package es.daw01.savex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("title", "SaveX - Iniciar sesión");
        return "login";
    }
    
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("title", "SaveX - Registrarse");
        return "register";
    }
}
