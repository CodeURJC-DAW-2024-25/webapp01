package es.daw01.savex.DTOs.products;

public record SearchProductRequest (
    String search,
    String supermarket,
    String keywords,
    Double minPrice,
    Double maxPrice,
    Integer limit,
    Integer page
){}
