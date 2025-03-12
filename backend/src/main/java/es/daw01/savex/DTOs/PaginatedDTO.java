package es.daw01.savex.DTOs;

import java.util.List;

public record PaginatedDTO<T>(
    List<T> data,
    long currentPage,
    long totalPages,
    long totalItems,
    boolean isLastPage
) {}