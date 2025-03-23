package es.daw01.savex.service;

import es.daw01.savex.DTOs.products.PriceDTO;
import es.daw01.savex.DTOs.products.ProductDTO;
import es.daw01.savex.DTOs.products.SearchProductRequest;
import es.daw01.savex.model.Product;
import es.daw01.savex.model.SupermarketType;
import es.daw01.savex.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    /**
     * Find a product by its id
     * 
     * @param id The id of the product
     * @return The product if found or an empty optional
    */
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    /**
     * Get a list of available supermarkets based on the current supermarket
     * 
     * @param currentSupermarket The current supermarket
     * @return A list of available supermarkets
    */
    public List<Map<String, Object>> getAvailableSupermarkets(String currentSupermarket) {

        return Arrays.stream(SupermarketType.values())
            .map(s -> Map.of(
                "name", (Object) s.getName(),
                "isSelected", (Object) s.getName().equalsIgnoreCase(currentSupermarket),
                "id", (Object) s.getName().toLowerCase()
            ))
            .collect(Collectors.toList());
    }

    /**
     * Get a list of available supermarkets
     * 
     * @return A list of available supermarkets as lowercase strings
    */
    public List<String> getSupermarkets() {
        return Arrays.stream(SupermarketType.values())
            .map(SupermarketType::getName)
            .map(String::toLowerCase)
            .collect(Collectors.toList());
    }

    /**
     * Generate a search product request based on a list of keywords
     * 
     * @param keywords The list of keywords
     * @return A search product request
    */
    public SearchProductRequest generateKeywordSearchProductRequest(List<String> keywords, int limit) {
        String joinedKeywords = String.join(", ", keywords);
        return new SearchProductRequest(
            null,
            null,
            joinedKeywords,
            0.0,
            0.0,
            limit,
            0
        );
    }

    /**
     * Generate a list of template products
     * 
     * @param count The number of products to generate
     * @return A list of template products
    */
    public List<ProductDTO> generateTemplateProducts(int count) {
        List<ProductDTO> templateProducts = new ArrayList<>(count);
        for (int i = 0; i < 5; i++) {
            templateProducts.add(new ProductDTO(
                "Supermarket",
                "0",
                "#",
                "Product",
                "product",
                "type",
                List.of("category"),
                new PriceDTO(0.0, 0.0, 0.0, "unit"),
                "#",
                "Brand",
                List.of("keyword")
            ));
        }
        
        return templateProducts;
    }
    
    public void save(Product product) {
        productRepository.save(product);
    }
}