package es.daw01.savex.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.DTOs.ShoppingListDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Product;
import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;
import es.daw01.savex.service.ApiService;
import es.daw01.savex.service.ShoppingListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api")
public class RestShoppingListController {
    
    @Autowired 
    private ShoppingListService shoppingListService;

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ApiService apiService;

    @GetMapping("/user-lists")
    public ResponseEntity<Map<String, Object>> getUserLists() {

        // Get the authenticated user
        User user = controllerUtils.getAuthenticatedUser();

        // Get the shopping lists of the user
        List<ShoppingList> lists = shoppingListService.findAllByUser(user);

        // Parse the shopping lists to DTOs
        List<ShoppingListDTO> listsDTO = shoppingListService.parseToDTOs(lists);

        // Return the shopping lists
        return ResponseEntity.ok(
            Map.of("data", listsDTO)
        );
    }

    @PostMapping("/user-lists/{id}/product/{productId}")
    public ResponseEntity<Map<String,Object>> addProductToList(@PathVariable Long id, @PathVariable String productId) {


        ProductDTO productDTO = apiService.fetchProduct(productId);

        // Get the authenticated user
        User user = controllerUtils.getAuthenticatedUser();

        // Get the shopping list
        Optional<ShoppingList> op = shoppingListService.findById(id);

        if (op.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ShoppingList list = op.get();

        // Check if the shopping list belongs to the user
        if (!list.getUser().equals(user)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Add the product to the shopping list
            shoppingListService.addProductToList(list, productDTO);

            // Return the shopping list
            return ResponseEntity.ok(Map.of("message", "Producto añadido correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
