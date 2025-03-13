package es.daw01.savex.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.PriceDTO;
import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.DTOs.products.SearchProductRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiService {
    private final String API_BASE_URL = "https://market-pricings-server.vercel.app/api/v2";
    private final RestTemplate restTemplate = new RestTemplate();

    private final int DEFAULT_SIZE = 10;

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

public ResponseEntity<PaginatedDTO<ProductDTO>> fetchProducts(SearchProductRequest request) {
    // Default values
    int page = Optional.ofNullable(request.page()).orElse(0);
    int limit = Optional.ofNullable(request.limit()).orElse(DEFAULT_SIZE);

    // Safe URL construction
    String apiUrl = API_BASE_URL + "/query";
    
    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
            .queryParamIfPresent("supermarket", Optional.ofNullable(request.supermarket()))
            .queryParamIfPresent("search", Optional.ofNullable(request.search()))
            .queryParamIfPresent("keywords", Optional.ofNullable(request.keywords()))
            .queryParamIfPresent("min_price", Optional.ofNullable(request.minPrice()))
            .queryParamIfPresent("max_price", Optional.ofNullable(request.maxPrice()))
            .queryParam("limit", limit)
            .queryParam("page", page); 

    String finalUrl = builder.toUriString(); // URL final correctamente construida

    return restTemplate.exchange(
            finalUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<PaginatedDTO<ProductDTO>>() {}
        );
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