package es.daw01.savex.service;

import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.products.ProductDTO;
import es.daw01.savex.DTOs.products.SearchProductRequest;
import es.daw01.savex.DTOs.products.SupermarketStatsDTO;

import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class ApiService {
    private final String API_BASE_URL = "https://market-pricings-server.vercel.app/api/v2";
    private final RestClient restClient = RestClient.create();
    private final int DEFAULT_SIZE = 10;

    public PaginatedDTO<ProductDTO> fetchProducts(SearchProductRequest request) {
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

        String finalUrl = builder.toUriString();

        return restClient.get()
            .uri(finalUrl)
            .retrieve()
            .body(new ParameterizedTypeReference<PaginatedDTO<ProductDTO>>() {}
        );
    }

    public long countProducts(String supermarket) {
        String apiUrl = API_BASE_URL + "/query?supermarket=" + supermarket;

        PaginatedDTO<ProductDTO> products = restClient.get()
            .uri(apiUrl)
            .retrieve()
            .body(new ParameterizedTypeReference<PaginatedDTO<ProductDTO>>() {}
        );

        return products.total_items();
    }

    /**
     * Fetches a product from the API
     * 
     * @param supermarket The supermarket to search in
     * @param id          The product ID
     * @return A product
     */
    public ProductDTO fetchProduct(String productId) {
        // Format the API URL
        String apiUrl = String.format("%s/product/%s", API_BASE_URL, productId);

        // Make the API request
        ProductDTO product = restClient.get()
            .uri(apiUrl)
            .retrieve()
            .body(ProductDTO.class);

        return product;
    }

    /**
     * Fetches stats from the API
     * 
     * @return A list of stats
     */
    public SupermarketStatsDTO fetchStats() {
        // Format the API URL
        String apiUrl = String.format("%s/stats", API_BASE_URL);

        // Make the API request
        SupermarketStatsDTO stats = restClient.get()
            .uri(apiUrl)
            .retrieve()
            .body(SupermarketStatsDTO.class);

        return stats;
    }
}