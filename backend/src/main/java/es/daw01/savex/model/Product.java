package es.daw01.savex.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String productUrl;

    @Column(nullable = false)
    private SupermarketType supermarket;

    @Column(nullable = false)
    private String productType;

    @Column(nullable = false)
    private String thumbnail;

    @ElementCollection
    private List<String> productCategories;

    @Column(nullable = false)
    private double priceTotal;

    @Column(nullable = false)
    private double pricePerReferenceUnit;

    @Column(nullable = false)
    private double referenceUnits;

    @Column(nullable = false)
    private String referenceUnitName;

    @Column(nullable = true)
    private String brand;

    @Column(nullable = true)
    private String normalizedName;

    @ElementCollection
    private List<String> keywords;

    @ManyToMany(mappedBy = "products")
    private List<ShoppingList> shoppingLists;

    // Constructors ----------------------------------------------------------->>
    public Product() {
        /* Used by Spring Data JPA */ }

    public Product(
            String name,
            String productId,
            String productUrl,
            SupermarketType supermarket,
            String productType,
            String thumbnail,
            List<String> productCategories,
            double priceTotal,
            double pricePerReferenceUnit,
            double referenceUnits,
            String referenceUnitName,
            String brand) {
        this.name = name;
        this.productId = productId;
        this.productUrl = productUrl;
        this.supermarket = supermarket;
        this.productType = productType;
        this.thumbnail = thumbnail;
        this.productCategories = productCategories;
        this.priceTotal = priceTotal;
        this.pricePerReferenceUnit = pricePerReferenceUnit;
        this.referenceUnits = referenceUnits;
        this.referenceUnitName = referenceUnitName;
        this.brand = brand;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Product product = (Product) obj;
        return this.productId.equals(product.getProductId()) && this.supermarket.equals(product.getSupermarket());
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public SupermarketType getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(SupermarketType supermarket) {
        this.supermarket = supermarket;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<String> productCategories) {
        this.productCategories = productCategories;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public double getPricePerReferenceUnit() {
        return pricePerReferenceUnit;
    }

    public void setPricePerReferenceUnit(double pricePerReferenceUnit) {
        this.pricePerReferenceUnit = pricePerReferenceUnit;
    }

    public double getReferenceUnits() {
        return referenceUnits;
    }

    public void setReferenceUnits(double referenceUnits) {
        this.referenceUnits = referenceUnits;
    }

    public String getReferenceUnitName() {
        return referenceUnitName;
    }

    public void setReferenceUnitName(String referenceUnitName) {
        this.referenceUnitName = referenceUnitName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
