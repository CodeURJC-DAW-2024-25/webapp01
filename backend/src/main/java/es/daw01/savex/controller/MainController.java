package es.daw01.savex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Post;
import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.service.PostService;
import es.daw01.savex.service.ShoppingListService;

@Controller
public class MainController {

    private static final int LANDING_POSTS = 4;

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private PostService postService;

    @Autowired
    private ShoppingListService shoppingListService;

    @GetMapping("/")
    public String getRootPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        // Add posts to the model
        List<Post> posts = postService
                .findByVisibilityOrderByCreatedAtDesc(VisibilityType.PUBLIC, PageRequest.of(0, LANDING_POSTS))
                .getContent();

        model.addAttribute("title", "SaveX - Ahorra dinero, tiempo y esfuerzo");
        model.addAttribute("extendedClass", "");
        model.addAttribute("searchQuery", "");
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/about")
    public String getAboutPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        model.addAttribute("title", "SaveX - Sobre nosotros");
        return "about";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        User user = controllerUtils.getAuthenticatedUser();
        List<ShoppingList> shoppingLists = shoppingListService.findAllByUser(user);

        // Add user data to the model
        controllerUtils.addUserDataToModel(model);

        model.addAttribute("title", "SaveX - ".concat(model.getAttribute("name").toString()));
        model.addAttribute("shoppingLists", shoppingLists);

        return "profile";
    }

}
