package es.daw01.savex.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;
import es.daw01.savex.service.ApiService;
import es.daw01.savex.service.ShoppingListService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/user-lists")
public class RestShoppingListController {

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ApiService apiService;

    @GetMapping({ "", "/" })
    public ResponseEntity<Map<String, Object>> getUserLists(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "4") int size
    ) {
        // Get the authenticated user
        User user = controllerUtils.getAuthenticatedUser();

        // Retrieve the shopping lists paginated
        Map<String, Object> response = shoppingListService.retrieveUserLists(
            user,
            PageRequest.of(page, size)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/product/{productId}")
    public ResponseEntity<Map<String, Object>> addProductToList(@PathVariable Long id, @PathVariable String productId) {

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

    @DeleteMapping("/{id}/product/{productId}")
    public ResponseEntity<Map<String, Object>> removeProductFromList(@PathVariable Long id,
            @PathVariable Long productId) {

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
            // Remove the product from the shopping list
            shoppingListService.removeProductFromList(list, productId);

            // Return the shopping list
            return ResponseEntity.ok(Map.of("message", "Producto eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
