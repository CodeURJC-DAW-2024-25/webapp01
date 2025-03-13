package es.daw01.savex.controller.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.DTOs.products.SearchProductRequest;
import es.daw01.savex.service.ApiService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/v1")
public class RestProductsController {

    @Autowired
    private ApiService apiService;


    @GetMapping("/products")
    public ResponseEntity<PaginatedDTO<ProductDTO>> getProducts(@ModelAttribute SearchProductRequest searchProductRequest)  {      
        return apiService.fetchProducts(searchProductRequest);
    }

}
