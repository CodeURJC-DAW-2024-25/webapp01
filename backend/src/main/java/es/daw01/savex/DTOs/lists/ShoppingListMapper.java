package es.daw01.savex.DTOs.lists;

import org.mapstruct.Mapper;

import es.daw01.savex.model.ShoppingList;

@Mapper(componentModel = "spring")
public interface ShoppingListMapper {
    public SimpleShoppingListDTO toSimpleDTO(ShoppingList shoppingList);
}
