package es.daw01.savex.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.daw01.savex.model.Product;
import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;
import es.daw01.savex.repository.ShoppingListRepository;

@Service
public class ShoppingListService {

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    /**
     * Find a shopping list by id
     * 
     * @param id The id of the shopping list
     * @return The optional shopping list
     */
    public Optional<ShoppingList> findById(long id) {
        return shoppingListRepository.findById(id);
    }

    /**
     * Save a shopping list
     * 
     * @param shoppingList The shopping list to save
     */
    public void save(ShoppingList shoppingList) {
        shoppingListRepository.save(shoppingList);
    }

    /**
     * Delete a shopping list by id
     * 
     * @param id The id of the shopping list
     */
    public void deleteById(long id) {
        shoppingListRepository.deleteById(id);
    }

    /**
     * Find all shopping lists by user
     * 
     * @param user The user to find the shopping lists
     */
    public List<ShoppingList> findAllByUser(User user) {
        return shoppingListRepository.findAllByUser(user);
    }

    /**
     * Create a shopping list
     * 
     * @param name        The name of the shopping list
     * @param description The description of the shopping list
     * @param user        The user of the shopping list
     */
    public ShoppingList createShoppingList(String name, String description, User user) {
        ShoppingList shoppingList = new ShoppingList(name, description, user, null);
        shoppingList.setName(name);
        shoppingList.setDescription(description);
        shoppingList.setUser(user);
        shoppingListRepository.save(shoppingList);
        return shoppingList;
    }

    /**
     * Add a product to a shopping list
     * 
     * @param shoppingList The shopping list to add the product
     * @param product      The product to add
     */
    public void addProduct(ShoppingList shoppingList, Product product) {
        shoppingList.getProducts().add(product);
        shoppingListRepository.save(shoppingList);
    }

    /**
     * Remove a product from a shopping list
     * 
     * @param shoppingList The shopping list to remove the product
     * @param product      The product to remove
     */
    public void removeProductById(ShoppingList shoppingList, Long productId) {
        shoppingList.getProducts().removeIf(product -> product.getId() == productId);
        shoppingListRepository.save(shoppingList);
    }

    public ShoppingList createShoppingList(String name, String description, User user, List<Product> products) {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName(name);
        shoppingList.setDescription(description);
        shoppingList.setUser(user);
        shoppingList.setProducts(products);
        shoppingListRepository.save(shoppingList);
        return shoppingListRepository.save(shoppingList);

    }

}
