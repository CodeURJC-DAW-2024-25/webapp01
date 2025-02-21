package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String postRegiserPage(UserDTO userDTO) {
        // TODO Validate the userDTO object

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
