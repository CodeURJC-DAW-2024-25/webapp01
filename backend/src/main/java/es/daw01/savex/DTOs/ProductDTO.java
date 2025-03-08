package es.daw01.savex.DTOs;

import java.util.List;

import es.daw01.savex.model.Product;

public class ProductDTO {
    private String _id;
    private String supermarket_name;
    private String product_id;
    private String product_url;
    private String display_name;
    private String normalized_name;
    private String product_type;
    private List<String> product_categories;
    private PriceDTO price;
    private String thumbnail;
    private String brand;
    private List<String> keywords;

    // Constructors ----------------------------------------------------------->>
    public ProductDTO() {
    }

    public ProductDTO(Product product){
        
        this._id = product.getProductId(); //TODO fix method getId() in Product
        this.supermarket_name = product.getSupermarket().getName();
        this.product_id = product.getProductId();
        this.product_url = product.getProductUrl();
        this.display_name = product.getName();
        this.product_type = product.getProductType();
        this.product_categories = product.getProductCategories();
        this.price = new PriceDTO(product.getPriceTotal(), product.getPricePerReferenceUnit(), product.getReferenceUnits(), product.getReferenceUnitName());
        this.thumbnail = product.getThumbnail();
        this.brand = product.getBrand();
        this.normalized_name = product.getNormalizedName();
        this.keywords = product.getKeywords();
    }


    // Getters and Setters ---------------------------------------------------->>
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String getSupermarket_name() {
        return supermarket_name;
    }
    public void setSupermarket_name(String supermarket_name) {
        this.supermarket_name = supermarket_name;
    }
    public String getProduct_id() {
        return product_id;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public String getProduct_url() {
        return product_url;
    }
    public void setProduct_url(String product_url) {
        this.product_url = product_url;
    }
    public String getDisplay_name() {
        return display_name;
    }
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }
    public String getProduct_type() {
        return product_type;
    }
    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }
    public List<String> getProduct_categories() {
        return product_categories;
    }
    public void setProduct_categories(List<String> product_categories) {
        this.product_categories = product_categories;
    }
    public PriceDTO getPrice() {
        return price;
    }
    public void setPrice(PriceDTO price) {
        this.price = price;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getNormalized_name() {
        return normalized_name;
    }
    public void setNormalized_name(String normalized_name) {
        this.normalized_name = normalized_name;
    }
    public List<String> getKeywords() {
        return keywords;
    }
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
