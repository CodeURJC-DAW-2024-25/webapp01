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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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

    @Operation(summary = "Get products")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Products fetched successfully",
            content = @Content(
                schema = @Schema(implementation = PaginatedDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = ApiResponseDTO.class)
            )
        )
    })   
    @GetMapping({"/", ""})
    public ResponseEntity<Object> getProducts(
        @ModelAttribute SearchProductRequest searchProductRequest
    )  {
        try {
        PaginatedDTO<ProductDTO> products = apiService.fetchProducts(searchProductRequest);
        return ApiResponseDTO.ok(products);
        } catch (Exception e) {
            return ApiResponseDTO.error(e.getMessage());
        }
    }


    @Operation(summary = "Get product by id")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product fetched successfully",
            content = @Content(
                schema = @Schema(implementation = ProductDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(
                schema = @Schema(implementation = ApiResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = ApiResponseDTO.class)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        ProductDTO product = apiService.fetchProduct(id);
        return ApiResponseDTO.ok(product);
    }


    @Operation(summary = "Compare products")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Products compared successfully",
            content = @Content(
                schema = @Schema(implementation = Map.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No products found",
            content = @Content(
                schema = @Schema(implementation = ApiResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = ApiResponseDTO.class)
            )
        )
    })
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

    @Operation(summary = "Suggest products")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Products suggested successfully",
            content = @Content(
                schema = @Schema(implementation = List.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No products found",
            content = @Content(
                schema = @Schema(implementation = ApiResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                schema = @Schema(implementation = ApiResponseDTO.class)
            )
        )
    })
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
