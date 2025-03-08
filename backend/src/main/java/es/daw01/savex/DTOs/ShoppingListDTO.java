package es.daw01.savex.DTOs;

import java.util.ArrayList;
import java.util.List;

import es.daw01.savex.model.Product;
import es.daw01.savex.model.ShoppingList;

public class ShoppingListDTO {
    private Long id;
    private String name;
    private String description;
    private int numberOfProducts;
    private List<ProductDTO> products;
    

    public ShoppingListDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public ShoppingListDTO() {
    }

    public ShoppingListDTO(ShoppingList shoppingList) {
        this.id = shoppingList.getId();
        this.name = shoppingList.getName();
        this.description = shoppingList.getDescription();
        this.numberOfProducts = shoppingList.getNumberOfProducts();
        this.products =  new ArrayList<ProductDTO>();
        for (Product product : shoppingList.getProducts()) {
            this.products.add(new ProductDTO(product));
        }
    }
    
    // Getters and Setters ---------------------------------------------------->>

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }
}
