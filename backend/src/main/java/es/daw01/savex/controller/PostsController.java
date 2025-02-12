package es.daw01.savex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;


@Controller
public class PostsController {
    
    @GetMapping("/posts")
    public String getPostsPage(Model model) {
        return "posts";
    }

    @GetMapping("/post/{id}")
    public String getPostPage(Model model) {
        return "post";
    }
}
