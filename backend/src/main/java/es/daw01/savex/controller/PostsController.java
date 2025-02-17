package es.daw01.savex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.service.PostService;

@Controller
public class PostsController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private PostService postService;
    
    @GetMapping("/posts")
    public String getPostsPage(Model model) {
        List<Post> posts = postService.findAll();

        // Remove private posts
        posts.removeIf(post -> post.getVisibility() == VisibilityType.PRIVATE);

        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        // Add template variables and render the view
        model.addAttribute("title", "SaveX - Aprende más con nuestras guías y posts");
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/posts/{postId}")
    public String getPostPage(Model model, @PathVariable long postId) {
        // Try to parse the id to a Long
        /*try {
            postId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return "redirect:/login";
        }*/

        // Get the post by id
        Post post = postService.findById(postId);

        // If the post is private, redirect to the posts page
        if (post.getVisibility() == VisibilityType.PRIVATE) {
            return "redirect:/posts";
        }

        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        // Add template variables and render the view
        model.addAttribute("title", "SaveX - " + post.getTitle());
        model.addAttribute("post", post);
        return "post-detail";
    }
}
