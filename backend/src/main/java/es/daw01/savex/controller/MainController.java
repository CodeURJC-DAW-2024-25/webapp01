package es.daw01.savex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;


@Controller
public class MainController {
    
    @GetMapping("/")
    public String getRootPage(Model model) {
        return "index";
    }
    
    @GetMapping("/about")
    public String getAboutPage(Model model) {
        return "about";
    }
}
