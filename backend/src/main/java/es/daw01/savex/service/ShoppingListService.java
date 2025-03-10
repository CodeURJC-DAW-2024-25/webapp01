package es.daw01.savex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.DTOs.ShoppingListDTO;
import es.daw01.savex.model.Product;
import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;
import es.daw01.savex.repository.ShoppingListRepository;

@Service
public class ShoppingListService {

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ProductService productService;

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
        shoppingListRepository.save(shoppingList);
        return shoppingList;
    }

    /**
     * Get the shopping lists of a user
     * 
     * @param user     The user to get the shopping lists
     * @param pageable The pageable object
     * @return The list of shopping lists
     */
    public Map<String, Object> retrieveUserLists(User user, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();

        // Retrieve shopping lists paginated
        Page<ShoppingList> listsPage = shoppingListRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);

        // Parse shopping lists to DTOs
        List<ShoppingListDTO> listsDTO = this.parseToDTOs(listsPage.getContent());

        // Generate response map
        response.put("data", listsDTO);
        response.put("currentPage", listsPage.getNumber());
        response.put("totalItems", listsPage.getTotalElements());
        response.put("totalPages", listsPage.getTotalPages());
        response.put("isLastPage", listsPage.isLast());

        return response;
    }

    /**
     * Add a product to a shopping list
     * 
     * @param shoppingList The shopping list to add the product
     * @param product      The product to add
     */
    public void addProductToList(ShoppingList shoppingList, ProductDTO productDTO) {
        Optional<Product> op = productService.findByProductDTO(productDTO);

        Product product = null;
        if (op.isEmpty()) {
            product = productService.createProductFromDTO(productDTO);
            productService.save(product);
        } else {
            product = op.get();
        }

        shoppingList.addProduct(product);
        shoppingListRepository.save(shoppingList);
    }

    /**
     * Parse a shopping list to a shopping list DTO
     * 
     * @param shoppingList The shopping list to parse
     * @return The shopping list DTO
     */
    public ShoppingListDTO parseToDTO(ShoppingList shoppingList) {
        return new ShoppingListDTO(shoppingList);
    }

    /**
     * Parse a list of shopping lists to a list of shopping list DTOs
     * 
     * @param shoppingLists The list of shopping lists to parse
     * @return The list of shopping list DTOs
     */
    public List<ShoppingListDTO> parseToDTOs(List<ShoppingList> shoppingLists) {

        ArrayList<ShoppingListDTO> shoppingListDTOs = new ArrayList<>();

        for (ShoppingList shoppingList : shoppingLists) {
            shoppingListDTOs.add(new ShoppingListDTO(shoppingList));
        }

        return shoppingListDTOs;
    }

    /**
     * Remove a product from a shopping list
     * 
     * @param shoppingList The shopping list to remove the product
     * @param product      The product to remove
     */
    public void removeProductFromList(ShoppingList shoppingList, Long productId) {
        Optional<Product> op = productService.findById(productId);

        // If the product does not exist, throw an exception
        if (op.isEmpty()) {
            throw new IllegalArgumentException("Product does not exist");
        }

        Product product = op.get();

        shoppingList.removeProduct(product);
        shoppingListRepository.save(shoppingList);
    }

}
