package es.daw01.savex.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Collections;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Comment;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.Product;
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
    /*
     * @PostMapping("/createPost")
     * public String createPost(
     * 
     * @RequestParam("title") String title,
     * 
     * @RequestParam("description") String description,
     * 
     * @RequestParam("author") String author,
     * 
     * @RequestParam("tags") String tags,
     * 
     * @RequestParam("date") String date,
     * 
     * @RequestParam("banner") MultipartFile banner) {
     * // Crear una nueva instancia de Post
     * Post newPost = new Post();
     * newPost.setTitle(title);
     * newPost.setDescription(description);
     * newPost.setAuthor(author);
     * newPost.setDate(date);
     * newPost.setTags(Collections.singletonList(tags)); // Convertimos tags en
     * lista
     * newPost.setVisibility(VisibilityType.PUBLIC); // Por defecto
     * 
     * // Guardar la imagen
     * if (!banner.isEmpty()) {
     * try {
     * String uploadDir = "uploads/";
     * File uploadPath = new File(uploadDir);
     * if (!uploadPath.exists()) {
     * uploadPath.mkdirs(); // Crea la carpeta si no existe
     * }
     * String filePath = uploadDir + banner.getOriginalFilename();
     * banner.transferTo(new File(filePath));
     * 
     * // Aquí podrías almacenar la URL en la base de datos si el modelo tiene un
     * campo
     * // `imageUrl`
     * } catch (IOException e) {
     * return "redirect:/createPost?error=upload";
     * }
     * }
     * 
     * // Guardar el post en la base de datos
     * postService.save(newPost);
     * 
     * // Redirigir a la página del post recién creado
     * return "redirect:/posts/" + newPost.getId();
     * }
     */
    /*
     * @PostMapping("/delete-post/{postId}")
     * public String removePost(Model model, @PathVariable long postId) {
     * User user = controllerUtils.getAuthenticatedUser();
     * Optional<Post> postOptional = postService.findById(postId);
     * if (postOptional.isEmpty()) {
     * throw new IllegalArgumentException("Post not found");
     * 
     * }
     * if (postOptional.get().getAuthor().equals(user) || user.getRole() ==
     * UserType.ADMIN) {
     * postService.deleteById(postId);
     * return "redirect:/";
     * 
     * }
     * 
     * return "/";
     * }
     */

    @PostMapping("/delete-post/{id}")
    public String removePost(Model model, @PathVariable long id) {
        try {
            User user = controllerUtils.getAuthenticatedUser();
            System.out.println("Usuario autenticado: " + (user != null ? user.getUsername() : "null"));

            Optional<Post> postOptional = postService.findById(id);
            if (postOptional.isEmpty()) {
                System.out.println("Post no encontrado: " + id);
                return "redirect:/posts?error=not_found";
            }

            Post post = postOptional.get();
            System.out.println("Post encontrado: " + post.getTitle());

            // Verificar si el usuario es el autor del post o un administrador
            if (post.getAuthor().equals(user.getUsername()) || user.getRole() == UserType.ADMIN) {
                postService.deletePost(id);
                System.out.println("Post eliminado correctamente.");
                return "redirect:/";
            } else {
                System.out.println("Usuario no autorizado para eliminar este post.");
                return "redirect:/posts?error=forbidden";
            }
        } catch (Exception e) {
            System.out.println("Error 500 al eliminar post: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/posts?error=server_error";
        }
    }

}