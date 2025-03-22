package es.daw01.savex.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.daw01.savex.DTOs.users.CreateUserRequest;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) boolean error, Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        if (error) {
            model.addAttribute("popupTitle", "Error al iniciar sesión");
            model.addAttribute("popupContent", "Usuario o contraseña incorrectos");
        }
        model.addAttribute("title", "SaveX - Iniciar sesión");
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        // Add the title to the model
        model.addAttribute("title", "SaveX - Registrarse");

        return "register";
    }

    @PostMapping("/register")
    public String postRegisterPage(
        @ModelAttribute CreateUserRequest createUserRequest,
        Model model
    ) {
        Map<String, String> errors = userService.validateUserAndReturnErrors(createUserRequest);

        // If there are errors, return the register page with the errors
        if (!errors.isEmpty()) {
            model.addAttribute("title", "SaveX - Registrarse");
            model.addAttribute("errors", errors);
            return "register";
        }

        // Try to save the user
        try {
            userService.register(createUserRequest);
        } catch (Exception e) {
            return "redirect:/register?error=exists";
        }

        // If successful, redirect to the login page
        return "redirect:/login";

    }
}
