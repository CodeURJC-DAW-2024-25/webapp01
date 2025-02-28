package es.daw01.savex.service;

import org.springframework.web.client.RestTemplate;

import es.daw01.savex.DTOs.ProductDTO;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiService {
    private final String API_BASE_URL = "https://market-pricings-server.vercel.app/api/v1/query";
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Fetches products from the API
     * 
     * @param searchInput The search input
     * @param supermarket The supermarket to search in
     * @return A list of products
    */
    public List<ProductDTO> fetchProducts(String searchInput, String supermarket) {
        System.out.println(searchInput);
        System.out.println(supermarket);
        // Format the API URL
        String apiUrl = String.format("%s?supermarket=%s&search=%s", API_BASE_URL, supermarket, searchInput);

        System.out.println(apiUrl);

        // Make the API request
        ResponseEntity<List<ProductDTO>> response = restTemplate.exchange(
            apiUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ProductDTO>>() {}
        );

        return response.getBody();
    }

    /**
     * Fetches a product from the API
     * 
     * @param id The product ID
     * @return The product
    */
    public ProductDTO fetchProduct(String supermarket, String id) {
        // Format the API URL
        String apiUrl = String.format("%s/%s?product_id=%s", API_BASE_URL, supermarket.toLowerCase(), id);

        // Make the API request
        ResponseEntity<List<ProductDTO>> response = restTemplate.exchange(
            apiUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ProductDTO>>() {}
        );

        return response.getBody().get(0);
    }
}
