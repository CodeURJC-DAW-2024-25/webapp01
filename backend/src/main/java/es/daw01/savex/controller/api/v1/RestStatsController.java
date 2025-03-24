package es.daw01.savex.controller.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.products.SupermarketStatsDTO;
import es.daw01.savex.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/stats")
public class RestStatsController {

    @Autowired
    private ProductService productService;
    
    @GetMapping("/products")
    public ResponseEntity<Object> getProductsStats() {
        try {
            SupermarketStatsDTO stats = productService.getSupermarketStats();
            return ApiResponseDTO.ok(stats);
        } catch (Exception e) {
            return ApiResponseDTO.error("There was an error fetching the stats");
        }
    }    
}