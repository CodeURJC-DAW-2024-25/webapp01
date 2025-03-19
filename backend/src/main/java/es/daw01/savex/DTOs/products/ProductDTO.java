package es.daw01.savex.DTOs.products;

import java.util.List;

public record ProductDTO(
    String _id,
    String supermarket_name,
    String product_id,
    String product_url,
    String display_name,
    String normalized_name,
    String product_type,
    List<String> product_categories,
    PriceDTO price,
    String thumbnail,
    String brand,
    List<String> keywords
) {}
