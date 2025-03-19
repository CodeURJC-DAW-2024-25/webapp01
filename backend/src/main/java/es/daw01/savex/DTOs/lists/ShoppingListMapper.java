package es.daw01.savex.DTOs.lists;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;

import es.daw01.savex.DTOs.products.ProductMapper;
import es.daw01.savex.model.ShoppingList;

@Mapper(componentModel = "spring", uses = { ProductMapper.class })
public interface ShoppingListMapper {
    public SimpleShoppingListDTO toSimpleDTO(ShoppingList shoppingList);
    public List<SimpleShoppingListDTO> toSimpleDTOs(Collection<ShoppingList> shoppingLists);

    public ShoppingListDTO toDTO(ShoppingList shoppingList);
    public List<ShoppingListDTO> toDTOs(Collection<ShoppingList> shoppingLists);
}
