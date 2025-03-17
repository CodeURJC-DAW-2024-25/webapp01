package es.daw01.savex.controller.api.v1;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.service.ShoppingListService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/lists")
public class RestShoppingListController {

    @Autowired
    private ShoppingListService shoppingListService;

    @GetMapping({ "", "/" })
    public ResponseEntity<Object> getUserLists(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        return shoppingListService.retrieveUserLists(PageRequest.of(page, size));
    }

    @PostMapping("/{id}/product/{productId}")
    public ResponseEntity<Object> addProductToList(
            @PathVariable Long id,
            @PathVariable String productId) {
        return shoppingListService.addProductToList(id, productId);
    }

    @DeleteMapping("/{id}/product/{productId}")
    public ResponseEntity<Object> removeProductFromList(
            @PathVariable Long id,
            @PathVariable String productId) {
        return shoppingListService.removeProductFromList(id, productId);
    }

}
