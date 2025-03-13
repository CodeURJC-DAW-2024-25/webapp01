package es.daw01.savex.DTOs.products;

import java.util.List;

import es.daw01.savex.DTOs.ProductDTO;

public record SearchProductResponse(
    List<ProductDTO> data,
    int total_items,
    int current_page,
    int total_pages,
    int items_per_page,
    boolean is_last_page
) {}