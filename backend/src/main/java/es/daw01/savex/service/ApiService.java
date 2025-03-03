package es.daw01.savex.service;

import org.springframework.web.client.RestTemplate; 

import es.daw01.savex.DTOs.ProductDTO;

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
}