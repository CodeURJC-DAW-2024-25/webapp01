package es.daw01.savex.service;

import org.springframework.web.client.RestTemplate;

import es.daw01.savex.DTOs.PriceDTO;
import es.daw01.savex.DTOs.ProductDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;  

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
  
@Service 
public class ApiService {
    private final String API_BASE_URL = "https://market-pricings-server.vercel.app/api/v2"; 
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetches products from the API
     * 
     * @param searchInput The search input
     * @param supermarket The supermarket to search in
     * @return A list of products
    */
private final Map<String, List<ProductDTO>> productCache = new HashMap<>();

public List<ProductDTO> getCachedProducts(String supermarket) {
    return productCache.getOrDefault(supermarket, List.of());
}

public void loadAllProductsIntoCache() {
    for (String supermarket : List.of("mercadona", "dia", "elcorteingles", "consum", "bm")) {
        List<ProductDTO> allProducts = new ArrayList<>();
        int page = 0;
        while (true) {
            ResponseEntity<Map<String, Object>> response = fetchProducts(null, supermarket, null, null, 1000, page);
            List<?> data = (List<?>) response.getBody().get("data");
            if (data == null || data.isEmpty()) break;

            List<ProductDTO> products = data.stream()
                .map(this::convertToProductDTO)
                .toList();

            allProducts.addAll(products);
            page++;
        }
        productCache.put(supermarket, allProducts);
       
    }
}


    public ResponseEntity<Map<String, Object>> fetchProducts(  
        String searchInput,
        String supermarket,
        Double minPrice,
        Double maxPrice,
        Integer limit, 
        Integer page
    ){ 
        // Set default values
        if (page == null) page = 0;
        if (limit == null) limit = 2000; 

      
        // Format the API URL
        String apiUrl = String.format("%s/query?", API_BASE_URL);

        if (supermarket != null) apiUrl = apiUrl.concat(String.format("supermarket=%s&", supermarket));
        if (searchInput != null) apiUrl = apiUrl.concat(String.format("search=%s&", searchInput));
        if (minPrice != null) apiUrl = apiUrl.concat(String.format("min_price=%s&", minPrice));
        if (maxPrice != null) apiUrl = apiUrl.concat(String.format("max_price=%s&", maxPrice));
        if (limit != null) apiUrl = apiUrl.concat(String.format("limit=%s&", limit));
        if (page != null) apiUrl = apiUrl.concat(String.format("page=%s&", page));
    
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            apiUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
    
        return response; 
    }

    /**
     * Fetches a product from the API
     * 
     * @param supermarket The supermarket to search in
     * @param id The product ID
     * @return A product
    */
    public ProductDTO fetchProduct(String productId) { 
        // Format the API URL
        String apiUrl = String.format("%s/product/%s", API_BASE_URL, productId);
        
        // Make the API request
        ProductDTO product = restTemplate.getForObject(apiUrl, ProductDTO.class);

        return product;
    }
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
    
            priceDTO.setTotal(totalPrice);
        } else {
            // Just in case, if "price" is not a map (malformed API), treat it as 0
            priceDTO.setTotal(0.0);
        }
    
        p.setPrice(priceDTO);
        return p;
    }
}