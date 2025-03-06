package es.daw01.savex.service;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Optional;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import es.daw01.savex.DTOs.ProductDTO;


@Service
public class ComparisonService {
    // Weights for each comparison factor
    private static final double NAME_WEIGHT = 0.6;
    private static final double BRAND_WEIGHT = 0.2;
    private static final double QUANTITY_WEIGHT = 0.1;

    // Adjust this threshold based on tests
    private static final double SIMILARITY_THRESHOLD = 0.4;

    // Public methods --------------------------------------------------------->>

    /**
     * Compare a target product with a list of candidates and return the best match
     * 
     * @param targetName Name of the target product
     * @param targetBrand Brand of the target product
     * @param candidates List of candidate products
     * @return The best match for the target product
    */
    public Optional<ProductDTO> findBestMatch(String targetName, String targetBrand, List<ProductDTO> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return Optional.empty();
        }

        ProductDTO bestMatch = null;
        double bestScore = 0;

        String normalizedTargetName = normalizeText(targetName);
        Optional<Double> targetQuantity = extractQuantity(targetName);

        for (ProductDTO candidate : candidates) {
            String normalizedCandidateName = normalizeText(candidate.getDisplay_name());
            Optional<Double> candidateQuantity = extractQuantity(candidate.getDisplay_name());

            double nameSimilarity = calculateSimilarity(normalizedTargetName, normalizedCandidateName);
            double brandSimilarity = compareBrands(targetBrand, candidate.getBrand());
            double quantitySimilarity = compareQuantities(targetQuantity, candidateQuantity);

            double weightedScore = 
                (nameSimilarity * NAME_WEIGHT) +
                (brandSimilarity * BRAND_WEIGHT) +
                (quantitySimilarity * QUANTITY_WEIGHT);

            if (weightedScore > bestScore) {
                bestScore = weightedScore;
                bestMatch = candidate;
            }
        }

        //Validate the best match
        if (bestScore >= SIMILARITY_THRESHOLD) {
            return Optional.of(bestMatch);
        } else {
            return Optional.empty();
        }
    }

    // Private methods -------------------------------------------------------->>

    /**
     * Compare two products and return the best match
     * 
     * @param targetBrand Brand of the target product
     * @param candidateBrand Brand of the candidate product
     * @return A score between 0.0 and 1.0 (0.0 = no match, 1.0 = exact match)
    */
    private double compareBrands(String targetBrand, String candidateBrand) {
        if (targetBrand == null || candidateBrand == null) return 0.5;
        return targetBrand.equalsIgnoreCase(candidateBrand) ? 1.0 : 0.0;
    }

    /**
     * Compare two quantities and return the similarity ratio
     * 
     * @param targetQuantity Quantity of the target product
     * @param candidateQuantity Quantity of the candidate product
     * @return A score between 0.0 and 1.0 (0.0 = no match, 1.0 = exact match)
    */
    private double compareQuantities(Optional<Double> targetQuantity, Optional<Double> candidateQuantity) {
        if (targetQuantity.isEmpty() || candidateQuantity.isEmpty()) return 0.5;
        double ratio = Math.min(targetQuantity.get(), candidateQuantity.get()) / 
                       Math.max(targetQuantity.get(), candidateQuantity.get());
        return ratio;
    }

    /**
     * Extract the quantity from a product name
     * 
     * @param name Product name
     * @return Quantity in grams, kilograms, liters, etc.
    */
    private Optional<Double> extractQuantity(String name) {
        try {
            String quantityRegex = "\\d+(\\.\\d+)?\\s*(kg|g|l|ml|cl|gr)";
            Matcher matcher = Pattern.compile(quantityRegex, Pattern.CASE_INSENSITIVE).matcher(name);

            // If a quantity is found, return it without units
            if (matcher.find()) {
                String numericPart = matcher.group(0).replaceAll("[^\\d.]", "");
                return Optional.of(Double.parseDouble(numericPart));
            }
        } catch (Exception e) {
            // Silence, it's okay if there's no quantity
        }

        return Optional.empty();
    }

    /**
     * Normalize text to remove accents and special characters
     * 
     * @param text Text to normalize
     * @return Normalized text
    */
    private String normalizeText(String text) {
        if (text == null) return "";

        // Normalize text to remove accents and special characters
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .toLowerCase().trim();
        
        return normalized.replaceAll("\\s+", " ");
    }

    /**
     * Calculate the similarity between two strings
     * 
     * @param text1 First text
     * @param text2 Second text
     * @return A score between 0.0 and 1.0 (0.0 = no match, 1.0 = exact match)
    */
    private double calculateSimilarity(String text1, String text2) {
        int maxLen = Math.max(text1.length(), text2.length());
        if (maxLen == 0) return 1.0;

        int distance = LevenshteinDistance.getDefaultInstance().apply(text1, text2);
        return 1.0 - ((double) distance / maxLen);
    }
}
