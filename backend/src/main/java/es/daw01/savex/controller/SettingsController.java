package es.daw01.savex.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.User;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingsController {

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping("/settings")
    public String getSettingsPage(Model model) {

        User user = controllerUtils.getAuthenticatedUser();
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("email",user.getEmail());
        model.addAttribute("title", "SaveX - ".concat(model.getAttribute("name").toString()));
        return "settings";
    }
}