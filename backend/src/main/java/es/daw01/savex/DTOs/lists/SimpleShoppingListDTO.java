package es.daw01.savex.DTOs.lists;

public record SimpleShoppingListDTO(
    long id,
    String name,
    String description
) {}