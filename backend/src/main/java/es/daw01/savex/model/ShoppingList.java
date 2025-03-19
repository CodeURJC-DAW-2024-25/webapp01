package es.daw01.savex.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class ShoppingList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int numberOfProducts;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Product> products;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    // Constructors ----------------------------------------------------------->>
    public ShoppingList() {
        this.createdAt = LocalDateTime.now();
    }

    public ShoppingList(String name, String description, User user, List<Product> products) {
        this.name = name;
        this.description = description;
        this.numberOfProducts = products != null ? products.size() : 0;
        this.user = user;
        this.products = products;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Add a product to the shopping list
     * 
     * @param product The product to add
    */
    public void addProduct(Product product) {
        this.products.add(product);
        this.numberOfProducts++;
    }

    /**
     * Remove a product from the shopping list
     * 
     * @param product The product to remove
    */
    public void removeProduct(Product product) {
        for (Product p : this.products) {
            if (p.equals(product)) {
                this.products.remove(p);
                break;
            }
        }

        this.numberOfProducts--;
    }

    // Getters and Setters ---------------------------------------------------->>
    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.numberOfProducts = products != null ? products.size() : 0;
        this.products = products;
    }

    public int getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(int numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

}
