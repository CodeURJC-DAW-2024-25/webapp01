package es.daw01.savex.DTOs;

import java.util.List;

public record PaginatedDTO<T>(
    List<T> page,
    long current_page,
    long total_pages,
    long total_items,
    long items_per_page,
    boolean is_last_page
) {}