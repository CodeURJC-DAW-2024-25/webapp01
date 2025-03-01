package es.daw01.savex.components;

import es.daw01.savex.DTOs.ProductDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope  
public class ProductSession {

    private List<ProductDTO> productList = new ArrayList<>();
    private String lastSearchInput;
    private String lastSupermarket;
    private Double lastMinPrice;
    private Double lastMaxPrice;

    public List<ProductDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductDTO> productList) {
        this.productList = productList;
    }

    public void clear() {
        productList.clear();
    }

    public void saveSearchParams(String searchInput, String supermarket, Double minPrice, Double maxPrice) {
        this.lastSearchInput = searchInput;
        this.lastSupermarket = supermarket;
        this.lastMinPrice = minPrice;
        this.lastMaxPrice = maxPrice;
    }

    public boolean hasSearchParamsChanged(String searchInput, String supermarket, Double minPrice, Double maxPrice) {
        return !equalsOrNull(lastSearchInput, searchInput) ||
               !equalsOrNull(lastSupermarket, supermarket) ||
               !equalsOrNull(lastMinPrice, minPrice) ||
               !equalsOrNull(lastMaxPrice, maxPrice);
    }

    //To compare the atributtes 
    private boolean equalsOrNull(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}
