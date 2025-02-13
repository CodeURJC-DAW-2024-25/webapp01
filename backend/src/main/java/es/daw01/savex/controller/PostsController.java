package es.daw01.savex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.daw01.savex.model.Post;
import es.daw01.savex.model.VisibilityEnum;
import es.daw01.savex.service.PostService;

@Controller
public class PostsController {

    @Autowired
    private PostService postService;
    
    @GetMapping("/posts")
    public String getPostsPage(Model model) {
        List<Post> posts = postService.findAll();

        // Remove private posts
        posts.removeIf(post -> post.getVisibility() == VisibilityEnum.PRIVATE);

        // Add template variables and render the view
        model.addAttribute("title", "SaveX - Aprende más con nuestras guías y posts");
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/posts/{id}")
    public String getPostPage(Model model, @PathVariable String id) {
        Long postId;

        // Try to parse the id to a Long
        try {
            postId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return "redirect:/login";
        }

        // Get the post by id
        Post post = postService.findById(postId);

        // If the post is private, redirect to the posts page
        if (post.getVisibility() == VisibilityEnum.PRIVATE) {
            return "redirect:/posts";
        }

        // Add template variables and render the view
        model.addAttribute("title", "SaveX - " + post.getTitle());
        model.addAttribute("post", post);
        return "post";
    }
}
