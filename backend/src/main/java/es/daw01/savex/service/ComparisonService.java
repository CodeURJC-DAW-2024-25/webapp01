package es.daw01.savex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.daw01.savex.DTOs.ProductDTO;

@Service
public class ComparisonService {
    private static final double NAME_WEIGHT = 0.6;
    private static final double BRAND_WEIGHT = 0.2;
    private static final double SIMILARITY_THRESHOLD = 0.4;

    @Autowired
    private ApiService apiService;

    /**
     * Compares products across multiple supermarkets based on the search input.
     * 
     * @param searchInput the search term for the products
     * @param supermarkets the list of supermarkets to search in
     * @param productService the service to map product data
     * @param limit the maximum number of products to fetch from each supermarket
     * @return a list of comparison results
     */
    public List<Map<String, Object>> compareProductsAcrossSupermarkets(String searchInput, List<String> supermarkets, ProductService productService, int limit) {
        Map<String, ProductDTO> comparisonMap = new HashMap<>();

        for (String market : supermarkets) {
            try {
                ResponseEntity<Map<String, Object>> response = apiService.fetchProducts(
                        searchInput, market, null, null, null, limit, 0
                );

                List<?> data = (List<?>) response.getBody().get("data");
                if (data != null && !data.isEmpty()) {
                    List<ProductDTO> candidates = data.stream()
                            .map(item -> productService.mapToProductDTO((Map<String, Object>) item))
                            .toList();

                    Optional<ProductDTO> bestMatch = findBestMatch(searchInput, null, candidates);

                    bestMatch.ifPresent(product -> comparisonMap.put(market, product));
                }
            } catch (Exception ex) {
                System.err.println("Error fetching products from " + market + ": " + ex.getMessage());
            }
        }

        return formatComparisonResults(supermarkets, comparisonMap);
    }

    /**
     * Formats the comparison results into a list of maps.
     * 
     * @param supermarkets the list of supermarkets
     * @param comparisonMap the map containing the best matched products for each supermarket
     * @return a list of formatted comparison results
     */
    private List<Map<String, Object>> formatComparisonResults(List<String> supermarkets, Map<String, ProductDTO> comparisonMap) {
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

        return comparisons;
    }

    /**
     * Finds the best matching product from a list of candidates based on the target name and brand.
     * 
     * @param targetName the target product name
     * @param targetBrand the target product brand
     * @param candidates the list of candidate products
     * @return an optional containing the best matched product, or empty if no match is found
     */
    public Optional<ProductDTO> findBestMatch(String targetName, String targetBrand, List<ProductDTO> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return Optional.empty();
        }

        ProductDTO bestMatch = null;
        double bestScore = 0;

        for (ProductDTO candidate : candidates) {
            double weightedScore = calculateWeightedScore(targetName, targetBrand, candidate);
            if (weightedScore > bestScore) {
                bestScore = weightedScore;
                bestMatch = candidate;
            }
        }

        return bestScore >= SIMILARITY_THRESHOLD ? Optional.of(bestMatch) : Optional.empty();
    }

    /**
     * Calculates the weighted score for a candidate product based on the target name and brand.
     * 
     * @param targetName the target product name
     * @param targetBrand the target product brand
     * @param candidate the candidate product
     * @return the weighted score
     */
    private double calculateWeightedScore(String targetName, String targetBrand, ProductDTO candidate) {
        double nameSimilarity = calculateSimilarity(targetName, candidate.getDisplay_name());
        double brandSimilarity = compareBrands(targetBrand, candidate.getBrand());

        return (nameSimilarity * NAME_WEIGHT) + (brandSimilarity * BRAND_WEIGHT);
    }

    /**
     * Calculates the similarity between two strings using the Levenshtein distance.
     * 
     * @param text1 the first string
     * @param text2 the second string
     * @return the similarity score between 0 and 1
     */
    private double calculateSimilarity(String text1, String text2) {
        int maxLen = Math.max(text1.length(), text2.length());
        if (maxLen == 0) return 1.0;

        int distance = org.apache.commons.text.similarity.LevenshteinDistance.getDefaultInstance().apply(text1, text2);
        return 1.0 - ((double) distance / maxLen);
    }

    /**
     * Compares two brands for similarity.
     * 
     * @param targetBrand the target brand
     * @param candidateBrand the candidate brand
     * @return the similarity score between 0 and 1
     */
    private double compareBrands(String targetBrand, String candidateBrand) {
        return (targetBrand != null && candidateBrand != null && targetBrand.equalsIgnoreCase(candidateBrand)) ? 1.0 : 0.5;
    }
}
