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
import java.util.List;

@Controller
public class ProductsController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ApiService apiService;

    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String searchInput, Model model) {
        List<ProductDTO> products = apiService.fetchProducts(searchInput, "mercadona");
        searchInput = searchInput == null ? "" : searchInput;

        // Set model attributes
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("searchQuery", searchInput);
        model.addAttribute("products", products);
        model.addAttribute("title", "SaveX - Productos");
        return "products";
    }

    @GetMapping("/products/{supermarket}/{id}")
    public String getProduct(@PathVariable String supermarket, @PathVariable String id, Model model) {
        ProductDTO product = apiService.fetchProduct(supermarket, id);
        
        // Set model attributes
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("product", product);
        model.addAttribute("title", "SaveX - " + product.getDisplay_name());
        return "product-detail";
    }
}
