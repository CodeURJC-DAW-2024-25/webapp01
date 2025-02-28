package es.daw01.savex.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserDTO;
import es.daw01.savex.service.UserService;
import jakarta.validation.Valid;


@Controller
public class SettingsController {

    @Autowired
    private ControllerUtils controllerUtils;
    
    @Autowired
    private UserService userService;

    @GetMapping("/settings")
    public String getSettingsPage(Model model) {

        User user = controllerUtils.getAuthenticatedUser();
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("email",user.getEmail());
        model.addAttribute("title", "SaveX - ".concat(model.getAttribute("name").toString()));
        return "settings";
    }

    @PostMapping("/update-account-data")
    public String postUpdateAccountInfo(
        @Valid @ModelAttribute("user") UserDTO userDTO, 
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
            return "settings";
        }
    // TODO revisar si esto sobra
        // Try to update the user
        try {
            userService.updateUserAccount(userDTO);
        } catch(Exception e) {
            return "redirect:/settings?error=exists";
        }

        // Redirect to the settings page
        return "redirect:/index";

}


@PostMapping("/delete-account")
    public String postDeleteAccount(Model model) {

        User user = controllerUtils.getAuthenticatedUser();
        user.getComments().forEach(comment -> comment.setAuthor(null));
        userService.deleteById(user.getId());
        return "/logout";
    }
}