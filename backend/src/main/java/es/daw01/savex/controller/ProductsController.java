package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.service.ApiService;

@Controller
public class ProductsController {
    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ApiService apiService;

    @GetMapping("/search")
    public String searchProducts(
        @RequestParam(required = false) String searchInput,
        @RequestParam(required = false) String supermarket,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Integer page,
        Model model
    ) {
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("searchQuery", searchInput);
        model.addAttribute("title", "SaveX - Productos");
        return "products";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable String id, Model model) {
        ProductDTO product = apiService.fetchProduct(id);

        controllerUtils.addUserDataToModel(model);
        model.addAttribute("product", product);
        
        model.addAttribute("title", "SaveX - " + product.getDisplay_name());
        return "product-detail";
    }

}