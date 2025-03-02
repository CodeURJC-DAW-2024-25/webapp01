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
        
        // Add flags for productType options
        model.addAttribute("isFresco", "fresco".equalsIgnoreCase(productType));
        model.addAttribute("isBebidas", "bebidas".equalsIgnoreCase(productType));
        model.addAttribute("isHigiene", "higiene".equalsIgnoreCase(productType));
        
        // Flags for supermarket (if needed)
        model.addAttribute("isMercadona", "mercadona".equalsIgnoreCase(supermarket));
        model.addAttribute("isCarrefour", "carrefour".equalsIgnoreCase(supermarket));
        model.addAttribute("isLidl", "lidl".equalsIgnoreCase(supermarket));

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
    
}
