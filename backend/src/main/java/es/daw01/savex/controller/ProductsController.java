package es.daw01.savex.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import es.daw01.savex.service.ComparisonService;
import es.daw01.savex.service.ProductService;

@Controller
public class ProductsController { 

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ProductService productService; 

    @Autowired 
    private ApiService apiService; 
    
    @Autowired
    private ComparisonService comparisonService;

    private static final int LIMIT_COMPARE_REQUEST = 5000;
 
    // Constructor or bean configuration (you can create it as @Component or initialize it manually)
    public ProductsController() {
        this.productService = new ProductService();
    }

    // Endpoint to compare products across supermarkets
    @GetMapping("/compare")
    public String compareProducts(@RequestParam(required = false) String searchInput, Model model) {
        controllerUtils.addUserDataToModel(model);

        List<String> supermarkets = productService.getSupermarkets();
        List<Map<String, Object>> comparisons = comparisonService.compareProductsAcrossSupermarkets(searchInput, supermarkets, productService, LIMIT_COMPARE_REQUEST);

        model.addAttribute("comparisons", comparisons);
        model.addAttribute("supermarkets", supermarkets);
        model.addAttribute("searchQuery", searchInput != null ? searchInput : "");

        return "compare-table";
    }

    // Endpoint to get the comparison table for specific products
    @GetMapping("/get-compare-table")
    public String getCompareTable(@RequestParam(required = false) String products, Model model) {

        controllerUtils.addUserDataToModel(model);
        List<Map<String, Object>> comparisons = new ArrayList<>();

        if (products != null) {
            String[] productIds = products.split("_");
            for (String productId : productIds) {
                String[] idSplit = productId.split("@");
                String supermarket_name = idSplit[0];
                String display_name = idSplit[1];
                String price = idSplit[2];
                String url = idSplit[3];
                // String product_id = idSplit[4];

                Map<String, Object> entry = new HashMap<>();

                entry.put("product_name", display_name);
                // entry.put("product_id", product_id);
                entry.put("product_url", url);
                entry.put("price", price);
                entry.put("supermarket", supermarket_name);

                comparisons.add(entry);
            }
        }

        model.addAttribute("comparisons", comparisons);

        return "compare-table";
    }

    // Endpoint to search for products with various filters
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

    // Endpoint to get details of a specific product by its ID
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
