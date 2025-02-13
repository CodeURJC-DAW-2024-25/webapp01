package es.daw01.savex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductsController {
    
    @GetMapping("/products")
    public String getProductsPage(Model model) {
        model.addAttribute("title", "SaveX - Productos");
        return "products";
    }    
    
}
