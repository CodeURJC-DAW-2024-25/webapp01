package es.daw01.savex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostsController {
    
    @GetMapping("/posts")
    public String getPostsPage(Model model) {
        model.addAttribute("title", "SaveX - Aprende más con nuestras guías y posts");
        return "posts";
    }

    @GetMapping("/post/{id}")
    public String getPostPage(Model model) {
        model.addAttribute("title", "SaveX - Post %id%");
        return "post";
    }
}
