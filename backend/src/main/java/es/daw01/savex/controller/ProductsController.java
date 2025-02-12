package es.daw01.savex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.core.model.Model;

@Controller
public class ProductsController {
    
    @GetMapping("/products")
    public String getProductsPage(Model model) {
        return "products";
    }    
    
}
