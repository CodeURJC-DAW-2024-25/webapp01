package es.daw01.savex.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.service.ApiService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api") 
public class RestProductsController {

    @Autowired
    private ApiService apiService;

    @GetMapping("/products")
    public ResponseEntity<Map<String, Object>> getProducts(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String supermarket,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) String productType,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) Integer page
    ) {
        System.out.println("search: " + search);
        System.out.println("supermarket: " + supermarket);
        System.out.println("minPrice: " + minPrice);
        System.out.println("maxPrice: " + maxPrice);
        System.out.println("productType: " + productType);
        System.out.println("limit: " + limit);
        System.out.println("page: " + page);
        return apiService.fetchProducts(
            search, supermarket, minPrice, maxPrice, productType, limit, page
        );
    }
}
