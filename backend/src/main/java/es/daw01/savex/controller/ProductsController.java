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
import java.util.Map;
import java.util.Optional;

@Controller
public class ProductsController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ApiService apiService;

    private List<Map<String, Object>> products;

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

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable String id, Model model) {
        
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("title", "SaveX - Producto");
        //Delete leading and trailing whitespaces
        final String trimmedId = id.trim();
        //Find the product with the given id
        Optional<Map<String, Object>> productOpt = products.stream()
        .filter(product -> trimmedId.equals(String.valueOf(product.get("product_id")).trim()))
        .findFirst();
    
        
        //If the product exists, add it to the model
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
        } else {
            model.addAttribute("product", null);
        }

        return "product-detail";

    }
}
