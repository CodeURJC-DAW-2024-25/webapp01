package es.daw01.savex.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.daw01.savex.DTOs.lists.CreateListRequest;
import es.daw01.savex.DTOs.lists.ShoppingListDTO;
import es.daw01.savex.DTOs.lists.ShoppingListMapper;
import es.daw01.savex.DTOs.lists.SimpleShoppingListDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;
import es.daw01.savex.service.ShoppingListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/shoppingList")
public class ShoppingListController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ShoppingListService shoppingListService;

    @Autowired
    private ShoppingListMapper shoppingListMapper;

    @GetMapping({ "", "/" })
    public String getShoppingLists(Model model) {
        User user = controllerUtils.getAuthenticatedUser();
        List<ShoppingList> shoppingLists = shoppingListService.findAllByUser(user);

        // Add attributes to the model
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("title", "SaveX - Tus listas de la compra");
        model.addAttribute("shoppingLists", shoppingLists);

        return "shoppingLists";
    }

    @GetMapping("/{id}")
    public String getShoppingList(@PathVariable Long id, Model model) {

        User user = controllerUtils.getAuthenticatedUser();

        Optional<ShoppingList> op = shoppingListService.findById(id);
        if (op.isEmpty())
            return "redirect:/";

        ShoppingList shoppingList = op.get();

        // Check if the user is the owner of the shopping list
        if (!shoppingList.getUser().equals(user))
            return "redirect:/";

        ShoppingListDTO shoppingListDTO = shoppingListMapper.toDTO(shoppingList);

        // Add attributes to the model
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("shoppingList", shoppingListDTO);
        model.addAttribute("title", "SaveX - Lista ");

        return "shoppingList-detail";
    }

    @PostMapping("/create")
    public String postMethodName(@RequestBody CreateListRequest request) {
        // Add the shopping list to the user
        SimpleShoppingListDTO newShoppingList = shoppingListService.createShoppingList(request);

        if (newShoppingList == null)
            return "redirect:/profile";
        return String.format("redirect:/shoppingList/%d", newShoppingList.id());
    }

    @PostMapping("/{id}/delete")
    public String deleteShoppingList(@PathVariable Long id) {
        shoppingListService.deleteById(id);
        return "redirect:/profile";
    }

}
