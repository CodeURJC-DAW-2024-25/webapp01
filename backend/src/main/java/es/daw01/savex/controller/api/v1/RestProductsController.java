package es.daw01.savex.controller.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.products.ProductDTO;
import es.daw01.savex.DTOs.products.SearchProductRequest;
import es.daw01.savex.components.ComparationAlgorithm;
import es.daw01.savex.service.ApiService;
import es.daw01.savex.service.ProductService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ProductService productService;

    @Autowired
    private ComparationAlgorithm comparationAlgorithm;

    @GetMapping({"/", ""})
    public ResponseEntity<Object> getProducts(
        @ModelAttribute SearchProductRequest searchProductRequest
    )  {
        PaginatedDTO<ProductDTO> products = apiService.fetchProducts(searchProductRequest);
        return ApiResponseDTO.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        ProductDTO product = apiService.fetchProduct(id);
        return ApiResponseDTO.ok(product);
    }

    @GetMapping("/{id}/compare")
    public ResponseEntity<Object> compareProducts(@PathVariable String id) {
        ProductDTO mainProduct = apiService.fetchProduct(id);
        SearchProductRequest searchProductRequest = productService.generateKeywordSearchProductRequest(
            mainProduct.keywords(),
            1000
        );

        // Fetch candidate products
        List<ProductDTO> candidateProducts = apiService.fetchProducts(searchProductRequest).page();
        Map<String, ProductDTO> result = comparationAlgorithm.compareProducts(mainProduct, candidateProducts);

        if (result.isEmpty()) {
            return ApiResponseDTO.error("No products found");
        }

        return ApiResponseDTO.ok(result);
    }

    @GetMapping("data")
    public ResponseEntity<Object> getActivityData() {
        Map<String, Object> response = productService.getActivityData1();
        if (response.isEmpty()) {
            return ApiResponseDTO.error("No data found");
        }
        return ApiResponseDTO.ok(response);
    }

    @GetMapping("/productschart")
    public ResponseEntity<Object> getActivityData2() {
        Map<String, Object> response = productService.getActivityData2();
        if (response.isEmpty()) {
            return ApiResponseDTO.error("No data found");
        }
    return ApiResponseDTO.ok(response);
}

    @GetMapping("/{id}/suggested")
    public ResponseEntity<Object> suggestProducts(@PathVariable String id) {
        ProductDTO mainProduct = apiService.fetchProduct(id);
        SearchProductRequest searchProductRequest = productService.generateKeywordSearchProductRequest(
            mainProduct.keywords(),
            5
        );

        // Fetch candidate products
        List<ProductDTO> candidateProducts = apiService.fetchProducts(searchProductRequest).page();

        if (candidateProducts.isEmpty()) {
            return ApiResponseDTO.error("No products found");
        }

        return ApiResponseDTO.ok(candidateProducts);
    }
}
