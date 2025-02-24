package es.daw01.savex.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Binding;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.daw01.savex.utils.ValidationUtils;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.UserDTO;
import es.daw01.savex.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpServletRequest request) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

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
    public String postRegiserPage(
        @Valid @ModelAttribute UserDTO userDTO, 
        BindingResult bindingResult, 
        Model model) {
        
        if (bindingResult.hasErrors()) {
            // In case of errors, return to the form with the errors mapped
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            model.addAttribute("title", "SaveX - Registrarse");
            model.addAttribute("user", userDTO);
            model.addAttribute("errors", errors);
            return "register";
        }

        // TODO revisar si esto sobra
        // Try to save the user
        try {
            userService.registerNewUser(userDTO);

        } catch(Exception e) {
            return "redirect:/register?error=exists";
        }

        // If successful, redirect to the login page
        return "redirect:/login";

    }
}
