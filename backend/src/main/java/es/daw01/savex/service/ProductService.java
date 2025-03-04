package es.daw01.savex.service;

import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.model.SupermarketType;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private static final double SIMILARITY_THRESHOLD = 0.4; // You can adjust it based on tests

    // Weights for each attribute
    private static final double NAME_WEIGHT = 0.6;
    private static final double BRAND_WEIGHT = 0.2;
    private static final double QUANTITY_WEIGHT = 0.1;

    public List<Map<String, Object>> getAvailableSupermarkets(String currentSupermarket) {
        return Arrays.stream(SupermarketType.values())
            .map(s -> Map.of(
                "name", (Object) s.getName(),
                "isSelected", (Object) s.getName().equalsIgnoreCase(currentSupermarket),
                "id", (Object) s.getName().toLowerCase()
            ))
            .collect(Collectors.toList());
    }

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

    private double compareBrands(String targetBrand, String candidateBrand) {
        if (targetBrand == null || candidateBrand == null) return 0.5; // Neutral if no brand
        return targetBrand.equalsIgnoreCase(candidateBrand) ? 1.0 : 0.0; // Exact or nothing
    }

    private double compareQuantities(Optional<Double> targetQuantity, Optional<Double> candidateQuantity) {
        if (targetQuantity.isEmpty() || candidateQuantity.isEmpty()) return 0.5; // Neutral if no quantity
        double ratio = Math.min(targetQuantity.get(), candidateQuantity.get()) / 
                       Math.max(targetQuantity.get(), candidateQuantity.get());
        return ratio;  // The closer the quantities, the closer to 1.0
    }

    private Optional<Double> extractQuantity(String name) {
        try {
            String quantityRegex = "\\d+(\\.\\d+)?\\s*(kg|g|l|ml|cl|gr)";
            var matcher = java.util.regex.Pattern.compile(quantityRegex, java.util.regex.Pattern.CASE_INSENSITIVE).matcher(name);
            if (matcher.find()) {
                String numericPart = matcher.group(0).replaceAll("[^\\d.]", ""); // Remove units, keep numbers
                return Optional.of(Double.parseDouble(numericPart));
            }
        } catch (Exception e) {
            // Silence, it's okay if there's no quantity
        }
        return Optional.empty();
    }

    private String normalizeText(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^a-zA-Z0-9 ]", "")
                .toLowerCase().trim();
        return normalized.replaceAll("\\s+", " ");
    }

    private double calculateSimilarity(String text1, String text2) {
        int maxLen = Math.max(text1.length(), text2.length());
        if (maxLen == 0) return 1.0;

        int distance = LevenshteinDistance.getDefaultInstance().apply(text1, text2);
        return 1.0 - ((double) distance / maxLen);
    }
}
