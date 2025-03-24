package es.daw01.savex.DTOs.products;

import java.util.List;

public record SupermarketStatsDTO(
    List<SupermarketDTO> stats
) {}