package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.daw01.savex.components.ControllerUtils;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // --- Services ---
    @Autowired
    private ControllerUtils controllerUtils;

    // --- Methods ---
    @GetMapping({ "", "/" })
    public String getAdminPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        // Add data to the model
        model.addAttribute("title", "SaveX - Panel de administración");
        return "admin";
    }
}
