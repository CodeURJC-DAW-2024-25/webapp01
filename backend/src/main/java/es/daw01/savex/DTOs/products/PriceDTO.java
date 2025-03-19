package es.daw01.savex.DTOs.products;

public record PriceDTO(
    double total,
    double per_reference_unit,
    double reference_units,
    String reference_unit_name
) {}
