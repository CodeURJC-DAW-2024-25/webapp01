package es.daw01.savex.components;

import org.springframework.stereotype.Component;

import es.daw01.savex.DTOs.products.ProductDTO;
import es.daw01.savex.utils.LevenshteinUtils;

import java.util.*;

@Component
public class ComparationAlgorithm {

    /**
     * Compare a main product with a list of candidate products and return the best product by supermarket
     * 
     * @param mainProduct Main product to compare with the list of candidate products
     * @param candidateProducts List of products to compare with the main product
     * @return bestBySupermarket
     */
    public Map<String, ProductDTO> compareProducts(ProductDTO mainProduct, List<ProductDTO> candidateProducts) {
        Map<String, Double> productsScore = new HashMap<>();

        for (ProductDTO product : candidateProducts) {
            String productBrandFallback = (product.brand() != null && !product.brand().isEmpty()) 
                ? product.brand()
                : (product.normalized_name().contains(mainProduct.brand()) ? mainProduct.brand() : "");

            // Calculate brand score
            double brandScore = LevenshteinUtils.calculateLevenshteinDistance(mainProduct.brand(), productBrandFallback);
            brandScore = brandScore / (double) mainProduct.brand().length();

            // Calculate name score
            double nameScore = LevenshteinUtils.calculateLevenshteinDistance(mainProduct.normalized_name(), product.normalized_name());
            nameScore = nameScore / (double) mainProduct.normalized_name().length();
            
            // Calculate keywords score
            double keywordsScore = calculateKeywordsDistance(mainProduct.keywords(), product.keywords());
            keywordsScore = keywordsScore / (double) mainProduct.keywords().size();

            double finalScore = keywordsScore + nameScore + brandScore;
            if (keywordsScore > 0.8 || nameScore > 0.8 || brandScore > 0.8) {
                finalScore = Double.POSITIVE_INFINITY;
            }

            productsScore.put(product.product_id(), finalScore);
        }

        // Sort candidate products by score
        candidateProducts.sort(Comparator.comparingDouble(product -> productsScore.get(product.product_id())));

        // Get the best product by supermarket
        return getBestProductBySupermarket(candidateProducts);
    }

    // Private methods -------------------------------------------------------->>

    private Map<String, ProductDTO> getBestProductBySupermarket(List<ProductDTO> candidateProducts) {
        Map<String, ProductDTO> bestBySupermarket = new HashMap<>();
        for (ProductDTO product : candidateProducts) {
            if (!bestBySupermarket.containsKey(product.supermarket_name())) {
                bestBySupermarket.put(product.supermarket_name(), product);
            }
        }
        
        return bestBySupermarket;
    }

    private double calculateKeywordsDistance(List<String> k1, List<String> k2) {
        int missing = 0;
        for (String keyword : k1) {
            if (!String.join(",", k2).contains(keyword)) {
                missing++;
            }
        }
        return missing;
    }
}