package es.daw01.savex.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.lists.CreateListRequest;
import es.daw01.savex.DTOs.lists.ShoppingListMapper;
import es.daw01.savex.DTOs.lists.SimpleShoppingListDTO;
import es.daw01.savex.DTOs.products.ProductDTO;
import es.daw01.savex.DTOs.products.ProductMapper;
import es.daw01.savex.DTOs.lists.ListResponse;
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
    public ResponseEntity<Object> deleteById(long id) {
        shoppingListRepository.deleteById(id);
        return ApiResponseDTO.ok("Shopping list deleted successfully");
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
            listsPage.isLast()
        );
    }

    /**
     * Add a product to a shopping list
     * 
     * @param shoppingList The shopping list to add the product
     * @param product      The product to add
     */
    public ResponseEntity<Object> addProductToList(Long listId, String productId) {
        ProductDTO productDTO = apiService.fetchProduct(productId);

        // Get the authenticated user
        User user = controllerUtils.getAuthenticatedUser();

        // Get the shopping list
        Optional<ShoppingList> opList = findById(listId);
        if (opList.isEmpty()) {
            return ApiResponseDTO.error("Shopping list not found");
        }

        ShoppingList list = opList.get();

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
        shoppingListRepository.save(list);

        // Create response
        List<ProductDTO> productList = productMapper.toDTOs(list.getProducts());
        ListResponse<ProductDTO> response = new ListResponse<>(list.getId(), user.getUsername(), productList);

        return ApiResponseDTO.ok(response);
    }

    /**
     * Remove a product from a shopping list
     * 
     * @param shoppingList The shopping list to remove the product
     * @param product      The product to remove
     */
    public ResponseEntity<Object> removeProductFromList(Long id, String productId) {

        ProductDTO productDTO = apiService.fetchProduct(productId);
        // Get the authenticated user
        User user = controllerUtils.getAuthenticatedUser();

        // Get the shopping list
        Optional<ShoppingList> op1 = findById(id);

        if (op1.isEmpty()) {
            return ApiResponseDTO.error("Shopping list not found");
        }

        ShoppingList list = op1.get();

        // Check if the shopping list belongs to the user
        if (!list.getUser().equals(user)) {
            return ApiResponseDTO.error("Shopping list does not belong to the user");
        }

        Optional<Product> op2 = productService.findByProductDTO(productDTO);

        // If the product does not exist, throw an exception
        if (op2.isEmpty()) {
            return ApiResponseDTO.error("Product not found");
        }

        Product product = op2.get();

        list.removeProduct(product);
        shoppingListRepository.save(list);
        return ApiResponseDTO.ok("Product removed from list");
    }

}
