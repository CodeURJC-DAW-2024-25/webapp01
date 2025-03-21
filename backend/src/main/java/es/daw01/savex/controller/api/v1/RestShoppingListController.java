package es.daw01.savex.controller.api.v1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.lists.CreateListRequest;
import es.daw01.savex.DTOs.lists.ShoppingListDTO;
import es.daw01.savex.DTOs.lists.SimpleShoppingListDTO;
import es.daw01.savex.service.ShoppingListService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/lists")
public class RestShoppingListController {

    @Autowired
    private ShoppingListService shoppingListService;

    @GetMapping({ "", "/" })
    public ResponseEntity<Object> getUserLists(
        @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        PaginatedDTO<SimpleShoppingListDTO> lists = shoppingListService.retrieveUserLists(pageable);
        return ApiResponseDTO.ok(lists);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<Object> newList(
        @RequestBody CreateListRequest request
    ) {
        SimpleShoppingListDTO list = shoppingListService.createShoppingList(request);
        return ApiResponseDTO.ok(list);
    }

    @PostMapping("/{id}/product/{productId}")
    public ResponseEntity<Object> addProductToList(
        @PathVariable Long id,
        @PathVariable String productId
    ) {
        try{
            ShoppingListDTO slist = shoppingListService.addProductToList(id, productId);
            return ApiResponseDTO.ok(slist);
        } catch (Exception e) {
            return ApiResponseDTO.error("Error adding product to list");
        }
    }

    @DeleteMapping("/{id}/product/{productId}")
    public ResponseEntity<Object> removeProductFromList(
        @PathVariable Long id,
        @PathVariable String productId
    ) {
        try{
            SimpleShoppingListDTO shoppingList = shoppingListService.removeProductFromList(id, productId);
            return ApiResponseDTO.ok(shoppingList);
        }  catch (Exception e) {
            return ApiResponseDTO.error("Error removing product from list");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeList(
        @PathVariable Long id
    ) {
        try{
            return ApiResponseDTO.ok(shoppingListService.deleteById(id));
        } catch (Exception e) {
            return ApiResponseDTO.error("Error removing list");
        }
    }

}
