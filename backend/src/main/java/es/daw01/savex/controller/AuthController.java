package es.daw01.savex.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpServletRequest request) {
        // Get user CSRF token from login form
        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        
        model.addAttribute("token", token.getToken());
        model.addAttribute("title", "SaveX - Iniciar sesión");
        return "login";
    }
    
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("title", "SaveX - Registrarse");
        return "register";
    }
}
