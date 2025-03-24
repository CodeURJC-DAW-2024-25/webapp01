package es.daw01.savex.controller.api.v1;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.products.SupermarketStatsDTO;
import es.daw01.savex.service.ProductService;
import es.daw01.savex.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1/stats")
public class RestStatsController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;
    

    @Operation(summary = "Get the stats of the products")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stats fetched successfully",
            content = {@Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = SupermarketStatsDTO.class)
            )}
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was an error fetching the stats",
            content = @Content
        )
    })
    @GetMapping("/products")
    public ResponseEntity<Object> getProductsStats() {
        try {
            SupermarketStatsDTO stats = productService.getSupermarketStats();
            return ApiResponseDTO.ok(stats);
        } catch (Exception e) {
            return ApiResponseDTO.error("There was an error fetching the stats");
        }
    }

    @Operation(summary = "Get the stats of the users")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stats fetched successfully",
            content = {@Content(
                mediaType = "application/json", 
                schema = @Schema(implementation = SupermarketStatsDTO.class)
            )}
        ),
        @ApiResponse(
            responseCode = "500",
            description = "There was an error fetching the stats",
            content = @Content
        )
    })
    @GetMapping("/users")
    public ResponseEntity<Object> getUsersStats() {
        try {
            return ApiResponseDTO.ok(userService.getUsersStats());
        } catch (Exception e) {
            return ApiResponseDTO.error("There was an error fetching the stats");
        }
    }
}