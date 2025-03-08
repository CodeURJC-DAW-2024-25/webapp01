package es.daw01.savex.service;

import es.daw01.savex.DTOs.PriceDTO;
import es.daw01.savex.DTOs.ProductDTO;
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
    private ComparisonService comparisonService;

    @Autowired
    private ProductRepository productRepository;

    
    /**
     * Find a product by its id
     * 
     * @param id The id of the product
     * @return The product if found or an empty optional
    */
    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    /**
     * Find the best match for a product based on the target name and brand
     * 
     * @param targetName The target name
     * @param targetBrand The target brand
     * @param candidates The list of candidates
     * @return The best match
    */
    public Optional<ProductDTO> findBestMatch(String targetName, String targetBrand, List<ProductDTO> candidates) {
        return comparisonService.findBestMatch(targetName, targetBrand, candidates);
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
    
    public ProductDTO mapToProductDTO(Map<String, Object> map) {
        ProductDTO p = new ProductDTO();
        p.setDisplay_name(map.get("display_name") != null ? (String) map.get("display_name") : (String) map.get("name"));
        p.setSupermarket_name((String) map.get("supermarket_name"));

        PriceDTO priceDTO = new PriceDTO();
        if (map.get("price") instanceof Map) {
            Map<String, Object> priceMap = (Map<String, Object>) map.get("price");
            Double totalPrice = priceMap.get("total") instanceof Number
                ? ((Number) priceMap.get("total")).doubleValue()
                : 0.0;

            priceDTO.setTotal(totalPrice);
        } else {
            priceDTO.setTotal(0.0);
        }

        p.setPrice(priceDTO);
        return p;
    }   

    public Product createProductFromDTO(ProductDTO productDTO) {
        return new Product(
            productDTO.getDisplay_name(),
            productDTO.getProduct_id(),
            productDTO.getProduct_url(),
            SupermarketType.fromString(productDTO.getSupermarket_name()),
            productDTO.getProduct_type(),
            productDTO.getThumbnail(),
            productDTO.getProduct_categories(),
            productDTO.getPrice().getTotal(),
            productDTO.getPrice().getPer_reference_unit(),
            productDTO.getPrice().getReference_units(),
            productDTO.getPrice().getReference_unit_name(),
            productDTO.getBrand()
        );
    }

    /**
     * Find a product by its product DTO
     * 
     * @param productDTO The product DTO
     * @return The product if found or an empty optional
    */
    public Optional<Product> findByProductDTO(ProductDTO productDTO) {
        SupermarketType supermarketType;
        String supermarketName = productDTO.getSupermarket_name();
        String productId = productDTO.getProduct_id();

        supermarketType = SupermarketType.fromString(supermarketName);
        
        return findBySupermarketAndProductId(supermarketType, productId);
    }
    
    public void save(Product product) {
        productRepository.save(product);
    }

    // Private methods -------------------------------------------------------->>
    
    private Optional<Product> findBySupermarketAndProductId(SupermarketType supermarketType, String productId) {
        return productRepository.findBySupermarketAndProductId(supermarketType, productId);
    }
}
