package es.daw01.savex.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.DTOs.PriceDTO;
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
        
    @GetMapping("/compare")
    public String compareProducts(@RequestParam(required = false) String searchInput, Model model) {
        controllerUtils.addUserDataToModel(model);
        
        // List of supermarkets to compare
        List<String> supermarkets = Arrays.asList("mercadona", "dia", "elcorteingles","consum","bm");

        // Map to store the product found for each supermarket
        Map<String, ProductDTO> comparisonMap = new HashMap<>();

        for (String market : supermarkets) {
            try {
                ResponseEntity<Map<String, Object>> response = apiService.fetchProducts(
                    searchInput, market, null, null, 1, 0
                );
                List<?> data =  (List<?>) response.getBody().get("data");
                if (data != null && !data.isEmpty()) {
                    ProductDTO product = convertToProductDTO(data.get(0));
                    comparisonMap.put(market, product);
                }
            } catch (Exception ex) {
                System.err.println("Error obtaining product from " + market + ": " + ex.getMessage());
            } 
        } 
      
        List<Map<String, Object>> comparisons = new ArrayList<>();
       
        for (String market : supermarkets) {
            Map<String, Object> entry = new HashMap<>();
            ProductDTO product = comparisonMap.get(market);
        
            entry.put("market", market);
        
            if (product != null) {
                entry.put("product_name", product.getDisplay_name() != null ? product.getDisplay_name() : "No disponible");
                entry.put("price", (product.getPrice() != null && product.getPrice().getTotal() != null) ? product.getPrice().getTotal() : "-");
            } else {
                entry.put("product_name", "No disponible");
                entry.put("price", "-");
            }
            comparisons.add(entry);
        }
        
        model.addAttribute("comparisons", comparisons);   
        model.addAttribute("supermarkets", supermarkets);
        model.addAttribute("searchQuery", searchInput != null ? searchInput : "");

        return "compare-table";
    }

    // Helper method to convert an object (Map) to ProductDTO
    private ProductDTO convertToProductDTO(Object data) { 
        Map<String, Object> map = (Map<String, Object>) data;
        ProductDTO p = new ProductDTO();
        p.setDisplay_name(map.get("display_name") != null ? (String) map.get("display_name") : (String) map.get("name"));
        p.setSupermarket_name((String) map.get("supermarket_name"));
    
        // Process the price correctly (expecting a nested object)
        PriceDTO priceDTO = new PriceDTO();
        if (map.get("price") instanceof Map) {
            Map<String, Object> priceMap = (Map<String, Object>) map.get("price");
            Double totalPrice = priceMap.get("total") instanceof Number 
                ? ((Number) priceMap.get("total")).doubleValue()
                : 0.0;
    
            priceDTO.setTotal(totalPrice.toString());
        } else {
            // Just in case, if "price" is not a map (malformed API), treat it as 0
            priceDTO.setTotal("0.0");
        }
    
        p.setPrice(priceDTO);
        return p;
    }
}
