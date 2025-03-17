package es.daw01.savex.controller.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.DTOs.products.SearchProductRequest;
import es.daw01.savex.service.ApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/products")
public class RestProductsController {

    @Autowired
    private ApiService apiService;


    @GetMapping({"/", ""})
    public ResponseEntity<PaginatedDTO<ProductDTO>> getProducts(
        @ModelAttribute SearchProductRequest searchProductRequest
    )  {
        PaginatedDTO<ProductDTO> products = apiService.fetchProducts(searchProductRequest);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        ProductDTO product = apiService.fetchProduct(id);
        return ResponseEntity.ok(product);
    }
}
