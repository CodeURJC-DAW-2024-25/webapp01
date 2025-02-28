package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.PostService;
import es.daw01.savex.service.ProductService;
import es.daw01.savex.service.UserService;

@Controller
@RequestMapping("/admin")
public class RestAdminController {
    
    // --- Services ---
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    // @Autowired
    // private CommentService commentService;
    // @Autowired
    // private PostService postService;
    @Autowired
    private ControllerUtils controllerUtils;

    // --- Methods ---
    @GetMapping("/form-create-user")
    public String formCreateUser(Model model) {
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("title", "SaveX - Crear usuario");
        return "admin/form-create-user";
    }

    @GetMapping("/form-create-product")
    public String formCreateProduct() {
        return "admin/form-create-product";
    }


}
