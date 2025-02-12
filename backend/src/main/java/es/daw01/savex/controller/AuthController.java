package es.daw01.savex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;


@Controller
public class AuthController {

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "login";
    }
    
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        return "register";
    }
}
