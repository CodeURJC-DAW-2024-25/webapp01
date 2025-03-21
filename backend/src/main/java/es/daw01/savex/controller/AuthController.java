package es.daw01.savex.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.daw01.savex.DTOs.UserDTO;
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

        // Create a new UserDTO object and add it to the model
        model.addAttribute("user", new UserDTO());

        // Add the title to the model
        model.addAttribute("title", "SaveX - Registrarse");

        return "register";
    }

    @PostMapping("/register")
    public String postRegisterPage(
            @Valid @ModelAttribute UserDTO userDTO,
            BindingResult bindingResult,
            Model model) {

        Map<String, String> errors = new HashMap<>();

        if (bindingResult.getFieldError("username") != null) {
            errors.put("username", bindingResult.getFieldError("username").getDefaultMessage());
        }
        if (bindingResult.getFieldError("email") != null) {
            errors.put("email", bindingResult.getFieldError("email").getDefaultMessage());
        }
        if (bindingResult.getFieldError("password") != null) {
            errors.put("password", bindingResult.getFieldError("password").getDefaultMessage());
        }

        if (!errors.isEmpty()) {

            model.addAttribute("title", "SaveX - Registrarse");
            model.addAttribute("user", userDTO);
            model.addAttribute("errors", errors);
            return "register";
        }

        // Try to save the user
        try {
            userService.registerNewUser(userDTO);

        } catch (Exception e) {
            return "redirect:/register?error=exists";
        }

        // If successful, redirect to the login page
        return "redirect:/login";

    }
}
