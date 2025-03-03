package es.daw01.savex.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;
import es.daw01.savex.service.ShoppingListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/shoppingList")
public class ShoppingListController {

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ShoppingListService shoppingListService;

    @GetMapping({"", "/"})
    public String getShoppingLists(Model model){
        User user = controllerUtils.getAuthenticatedUser();
        List<ShoppingList> shoppingLists = shoppingListService.findAllByUser(user);

        // Add attributes to the model
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("title", "SaveX - Tus listas de la compra");
        model.addAttribute("shoppingLists", shoppingLists);

        return "shoppingLists";
    }
    
    @GetMapping("/{id}")
    public String getShoppingList(@PathVariable Long id, Model model){
        // Optional<ShoppingList> op = shoppingListService.findById(id);
        // if(op.isEmpty()) return "redirect:/";

        // ShoppingList shoppingList = op.get();

        // Add attributes to the model
        controllerUtils.addUserDataToModel(model);
        // model.addAttribute("shoppingList", shoppingList);
        model.addAttribute("title", "SaveX - Lista ");

        return "shoppingList-detail";
    }
    
}
