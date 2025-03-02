package es.daw01.savex.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.service.ApiService;
import es.daw01.savex.service.ProductService;

@Controller
public class ProductsController {
    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ProductService productService;

    @Autowired
    private ApiService apiService;

    @GetMapping("/search")
    public String searchProducts( 
        @RequestParam(required = false) String searchInput,
        @RequestParam(required = false) String supermarket,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) String productType,
        @RequestParam(required = false) Integer page,
        Model model
    ) {
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("searchQuery", searchInput != null ? searchInput : "");
        model.addAttribute("supermarket", supermarket != null ? supermarket : "");
        model.addAttribute("minPrice", minPrice != null ? minPrice : "");
        model.addAttribute("maxPrice", maxPrice != null ? maxPrice : "");
        model.addAttribute("productType", productType != null ? productType : "");
        model.addAttribute("page", page != null ? page : 0);
        
        // Parse available supermarkets
        List<Map<String, Object>> supermarkets = productService.getAvailableSupermarkets(supermarket);

        model.addAttribute("supermarkets", supermarkets);
        model.addAttribute("title", "SaveX - Products");
        return "products";
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable String id, 
                             @RequestParam(required = false) String searchInput,
                             Model model) {
        ProductDTO product = apiService.fetchProduct(id);
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("product", product);
        model.addAttribute("searchQuery", searchInput != null ? searchInput : "");

        model.addAttribute("title", "SaveX - " + product.getDisplay_name());
        return "product-detail";
    }


    @GetMapping("/products/custom")
    public String GenerateCustomProductCard(Model model, @RequestParam String name, @RequestParam String price, @RequestParam String src, @RequestParam String id){
        controllerUtils.addUserDataToModel(model);

        model.addAttribute("id", id);
        model.addAttribute("name", name);
        model.addAttribute("price", price);
        model.addAttribute("src", src);

        return "custom-product";
    }
        
    
}
