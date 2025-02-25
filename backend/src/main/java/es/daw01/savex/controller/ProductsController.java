package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
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

    @Autowired
    private ObjectMapper objectMapper;

    private List<Map<String, Object>> products;

    @PostMapping("/search") 
    public String search(@RequestParam("searchInput") String searchInput, Model model) {
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("title", "SaveX - Productos");
        model.addAttribute("searchQuery", searchInput);
 

        String productsJSON = apiService.fetchData(searchInput);

        try {
            
            products = objectMapper.readValue(productsJSON, new TypeReference<List<Map<String, Object>>>() {});
            model.addAttribute("products", products);
        } catch (Exception e) {
            products = List.of(); 
            model.addAttribute("products", products);
        }

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
