package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.components.ControllerUtils;

@Controller
public class ProductsController {

    @Autowired
    private ControllerUtils controllerUtils;
    
    @GetMapping("/products")
    public String getProductsPage(Model model) {
        // Add user data to the model
        controllerUtils.addUserDataToModel(model);
        
        model.addAttribute("title", "SaveX - Productos");
        return "products";
    }    
    
}
