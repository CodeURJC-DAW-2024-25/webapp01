package es.daw01.savex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.ShoppingListDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;
import es.daw01.savex.service.ShoppingListService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequestMapping("/api")
public class RestShoppingListController {
    
    @Autowired 
    private ShoppingListService shoppingListService;

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping("/user-lists")
    public ResponseEntity<List<ShoppingListDTO>> getUserLists() {

        // Get the authenticated user
        User user = controllerUtils.getAuthenticatedUser();

        // Get the shopping lists of the user
        List<ShoppingList> lists = shoppingListService.findAllByUser(user);

        // Parse the shopping lists to DTOs
        List<ShoppingListDTO> listsDTO = shoppingListService.parseToDTOs(lists);

        // Return the shopping lists
        return ResponseEntity.ok(listsDTO);
    }

}
