package es.daw01.savex.controller;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Comment;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.MarkdownService;
import es.daw01.savex.service.PostService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PostsController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private MarkdownService markdownService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/posts")
    public String getPostsPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        // Add template variables and render the view
        model.addAttribute("title", "SaveX - Aprende más con nuestras guías y posts");
        model.addAttribute("extendedClass", "extended");
        return "posts";
    }

    @GetMapping("/posts/{id}")
    public String getPostPage(Model model, @PathVariable long id) throws SQLException {
        // Get the post by id
        Optional<Post> op = postService.findById(id);

        if (op.isEmpty())
            throw new IllegalArgumentException("Post not found");

        Post post = op.get();

        // If the post is private, redirect to the posts page
        if (post.getVisibility() == VisibilityType.PRIVATE) {
            return "redirect:/posts";
        }

        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        // Add template variables and render the view
        model.addAttribute("title", "SaveX - " + post.getTitle());
        model.addAttribute("post", post);
        model.addAttribute("content", markdownService.renderMarkdown(post.getContent()));
        return "post";
    }

    @PostMapping("/posts/{id}/addComment")
    public String addComment(@PathVariable long id, @RequestParam String comment) {
        // Get the post by id
        Optional<Post> op = postService.findById(id);

        if (op.isEmpty())
            throw new IllegalArgumentException("Post not found");

        // Get the logged user
        User user = controllerUtils.getAuthenticatedUser();

        Post post = op.get();
        Comment newComment = new Comment(user, post, comment);

        // Save the post, user and comment by updating the database
        commentService.save(newComment);

        return "redirect:/posts/" + id;
    }

    @PostMapping("/posts/{id}/deleteComment/{commentId}")
    public String deleteComment(@PathVariable long id, @PathVariable long commentId) {
        // Get the post by id
        Optional<Post> op = postService.findById(id);

        // Check if the post exists and get it
        if (op.isEmpty())
            throw new IllegalArgumentException("Post not found");
        Post post = op.get();

        // Get the comment by id
        Comment comment = commentService.findById(commentId).get();
        if (comment == null)
            return "redirect:/posts/" + id;

        // Check if the comment belongs to the post
        if (!comment.getPost().equals(post))
            return "redirect:/posts/" + id;

        // Get the logged user
        User user = controllerUtils.getAuthenticatedUser();

        if (user.getRole() == UserType.ADMIN) {
            // Delete the comment
            commentService.deleteById(commentId);

            return "redirect:/posts/" + id;
        }

        // Check if the user is the owner of the comment
        if (comment.isAuthor(user)) {
            // Delete the comment
            commentService.deleteById(commentId);
            return "redirect:/posts/" + id;
        }

        // Redirect to the post page
        return "redirect:/posts/" + id;

    }

    @GetMapping("/createPost")
    public String createPost(Model model) {
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("title", "SaveX - Crea un nuevo post");
        return "create-Post";
    }

    @PostMapping("/createPost")
    public String createPost(
        @RequestParam String title,
        @RequestParam String description,
        @RequestParam String content,
        @RequestParam String author,
        @RequestParam String tags,
        @RequestParam String visibility,
        @RequestParam MultipartFile banner
    ) {
        System.out.println(title);
        System.out.println(description);
        System.out.println(content);
        System.out.println(author);
        System.out.println(tags);
        System.out.println(visibility);
        // Create a new post with the form data
        Post newPost = postService.createPost(title, description, content, author, tags, visibility);


        // Save the post in the database
        postService.save(newPost, banner);

        // Redirect to the new post page
        return "redirect:/posts/" + newPost.getId();
    }

    // @PostMapping("/delete/{postId}")
    // public String deletePost(@PathVariable Long postId) {
    //     postService.deletePost(postId); // Eliminar el post en el servicio
    //     return "redirect:/posts"; // Redirigir a la lista de posts después de la eliminación
    // }
}