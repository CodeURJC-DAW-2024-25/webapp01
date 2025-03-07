package es.daw01.savex.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    private static final int LIMIT_COMPARE_REQUEST = 5000;
 
    // Constructor or bean configuration (you can create it as @Component or initialize it manually)
    public ProductsController() {
        this.productService = new ProductService();
    }

    @GetMapping("/compare")
    public String compareProducts(@RequestParam(required = false) String searchInput, Model model) {
        controllerUtils.addUserDataToModel(model);
        List<String> supermarkets = productService.getSupermarkets();
        Map<String, ProductDTO> comparisonMap = new HashMap<>();

        for (String market : supermarkets) {
            try {
                ResponseEntity<Map<String, Object>> response = apiService.fetchProducts(
                    searchInput, market, null, null, LIMIT_COMPARE_REQUEST, 0 // Request up to 5 products to compare
                );

                List<?> data = (List<?>) response.getBody().get("data");
                if (data != null && !data.isEmpty()) {
                    List<ProductDTO> candidates = data.stream()
                    .map(item -> productService.mapToProductDTO((Map<String, Object>) item))
                    .toList();
                      
                    // Compare products using the new service
                    Optional<ProductDTO> bestMatch = productService.findBestMatch(searchInput, null, candidates);
                 
                    bestMatch.ifPresent(product -> comparisonMap.put(market, product));
                }
            } catch (Exception ex) {
                System.err.println("Error fetching products from " + market + ": " + ex.getMessage());
            }
        }

        List<Map<String, Object>> comparisons = new ArrayList<>();
        for (String market : supermarkets) {
            Map<String, Object> entry = new HashMap<>();
            ProductDTO product = comparisonMap.get(market);

            entry.put("market", market);
            if (product != null) {
                entry.put("product_name", product.getDisplay_name());
                entry.put("price", product.getPrice() != null ? product.getPrice().getTotal() : "-");
            } else {
                entry.put("product_name", "Not available");
                entry.put("price", "-");
            }
            comparisons.add(entry);
        }

        model.addAttribute("comparisons", comparisons);
        model.addAttribute("supermarkets", supermarkets);
        model.addAttribute("searchQuery", searchInput != null ? searchInput : "");

        return "compare-table";
    }

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

        // Template products to fill the page
        List<ProductDTO> templateProducts = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            templateProducts.add(new ProductDTO());
        }

        model.addAttribute("supermarkets", supermarkets);
        model.addAttribute("title", "SaveX - Products");
        model.addAttribute("templateProducts", templateProducts);
        return "products";
    }

    @GetMapping("/products/{id}") 
    public String getProduct(
        @PathVariable String id, 
        @RequestParam(required = false) String searchInput,
        Model model
    ) {
        
        ProductDTO product = apiService.fetchProduct(id);
       
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("product", product);
        model.addAttribute("searchQuery", searchInput != null ? searchInput : "");
        model.addAttribute("title", "SaveX - " + product.getDisplay_name());
        return "product-detail";
    }
}
