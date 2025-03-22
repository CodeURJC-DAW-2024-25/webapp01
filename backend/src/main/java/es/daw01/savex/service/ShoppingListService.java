package es.daw01.savex.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.lists.CreateListRequest;
import es.daw01.savex.DTOs.lists.ShoppingListMapper;
import es.daw01.savex.DTOs.lists.SimpleShoppingListDTO;
import es.daw01.savex.DTOs.products.ProductDTO;
import es.daw01.savex.DTOs.products.ProductMapper;
import es.daw01.savex.DTOs.lists.ShoppingListDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.Product;
import es.daw01.savex.model.ShoppingList;
import es.daw01.savex.model.User;
import es.daw01.savex.repository.ShoppingListRepository;

@Service
public class ShoppingListService {

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    @Autowired
    private ShoppingListMapper shoppingListMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ApiService apiService;

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private UserService userService;

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
    public ShoppingListDTO deleteById(long id) {
        ShoppingList shoppingList = shoppingListRepository.findById(id).orElseThrow();
        shoppingListRepository.deleteById(id);
        return shoppingListMapper.toDTO(shoppingList);
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
     */
    public SimpleShoppingListDTO createShoppingList(CreateListRequest request) {
        User user = userService.getAuthenticatedUser();
        String listName = request.name();
        String listDescription = request.description();

        ShoppingList shoppingList = new ShoppingList(listName, listDescription, user, null);
        return shoppingListMapper.toSimpleDTO(shoppingListRepository.save(shoppingList));
    }

    /**
     * Get the shopping lists of a user
     * 
     * @param user     The user to get the shopping lists
     * @param pageable The pageable object
     * @return The list of shopping lists
     */
    public PaginatedDTO<SimpleShoppingListDTO> retrieveUserLists(Pageable pageable) {
        User user = userService.getAuthenticatedUser();

        // Retrieve shopping lists paginated
        Page<ShoppingList> listsPage = shoppingListRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);

        // Parse shopping lists to DTOs
        List<SimpleShoppingListDTO> listsDTO = shoppingListMapper.toSimpleDTOs(listsPage.getContent());

        // Create response
        return new PaginatedDTO<SimpleShoppingListDTO>(
                listsDTO,
                listsPage.getNumber(),
                listsPage.getTotalPages(),
                listsPage.getTotalElements(),
                listsPage.getSize(),
                listsPage.isLast());
    }

    /**
     * Add a product to a shopping list
     * 
     * @param shoppingList The shopping list to add the product
     * @param product      The product to add
     */
    public ShoppingListDTO addProductToList(Long listId, String productId) {

        // Get the authenticated user
        User user = controllerUtils.getAuthenticatedUser();

        // Get the shopping list
        ShoppingList list = findById(listId).orElseThrow();

        // Check if the shopping list belongs to the user
        if (!list.getUser().equals(user)) {
            throw new RuntimeException("Shopping list does not belong to the user");
        }

        ProductDTO productDTO = apiService.fetchProduct(productId);

        // Check if the product already exists
        Optional<Product> op = productService.findByProductDTO(productDTO);
        Product product;
        if (op.isEmpty()) {
            product = productMapper.toEntity(productDTO);
            productService.save(product);
        } else {
            product = op.get();
        }

        list.addProduct(product);
        return shoppingListMapper.toDTO(shoppingListRepository.save(list));
    }

    /**
     * Remove a product from a shopping list
     * 
     * @param shoppingList The shopping list to remove the product
     * @param product      The product to remove
     */
    public SimpleShoppingListDTO removeProductFromList(Long id, String productId) {

        ProductDTO productDTO = apiService.fetchProduct(productId);
        // Get the authenticated user
        User user = controllerUtils.getAuthenticatedUser();

        // Get the shopping list
        ShoppingList list = findById(id).orElseThrow();

        // Check if the shopping list belongs to the user
        if (!list.getUser().equals(user)) {
            return null;
        }

        Product product = productService.findByProductDTO(productDTO).orElseThrow();

        list.removeProduct(product);
        shoppingListRepository.save(list);

        return shoppingListMapper.toSimpleDTO(list);
    }

    /**
     * Get the shopping list of a user
     * 
     * @param user The user to get the shopping list
     * @param id   The id of the shopping list
     * @return The shopping list
     */
    public ShoppingListDTO getListById(long id) {
        User user = controllerUtils.getAuthenticatedUser();

        ShoppingList shoppingList = shoppingListRepository.findById(id).orElseThrow();

        // Check if the user is the owner of the shopping list
        if (!shoppingList.getUser().equals(user))
            throw new RuntimeException("Shopping list does not belong to the user");

        return shoppingListMapper.toDTO(shoppingList);
    }

    /**
     * Update a shopping list
     * 
     * @param id      The id of the shopping list
     * @param request The request to update the shopping list
     * @return The updated shopping list
     */
    public ShoppingListDTO updateList(Long id, CreateListRequest request) {
        User user = controllerUtils.getAuthenticatedUser();

        ShoppingList shoppingList = shoppingListRepository.findById(id).orElseThrow();

        // Check if the user is the owner of the shopping list
        if (!shoppingList.getUser().equals(user))
            throw new RuntimeException("Shopping list does not belong to the user");

        shoppingList.setName(request.name());
        shoppingList.setDescription(request.description());

        return shoppingListMapper.toDTO(shoppingListRepository.save(shoppingList));

    }
}
