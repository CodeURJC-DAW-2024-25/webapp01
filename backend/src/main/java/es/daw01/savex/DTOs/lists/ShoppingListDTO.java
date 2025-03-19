package es.daw01.savex.DTOs.lists;

import java.util.List;

import es.daw01.savex.DTOs.products.ProductDTO;

public record ShoppingListDTO(
    Long id,
    String name,
    String description,
    int numberOfProducts,
    List<ProductDTO> products
) {}