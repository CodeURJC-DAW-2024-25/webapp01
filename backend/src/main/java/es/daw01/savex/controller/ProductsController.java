package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import es.daw01.savex.DTOs.PriceDTO;
import es.daw01.savex.DTOs.ProductDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.components.ProductSession;
import es.daw01.savex.service.ApiService;
import java.util.List;

@Controller
public class ProductsController {
    private final static int PRODUCTS_PER_PAGE = 15;

    @Autowired
    private ControllerUtils controllerUtils;

    @Autowired
    private ApiService apiService;

    @Autowired
    private ProductSession productSession;

    @GetMapping("/search")
    public String searchProducts(
        @RequestParam(required = false) String searchInput,
        @RequestParam(required = false) String supermarket,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) Integer page,
        Model model
    ) {
        
        int currentPage = (page != null) ? page : 0;

        // If the search input is empty, we show all products
        if (currentPage == 0 || productSession.hasSearchParamsChanged(searchInput, supermarket, minPrice, maxPrice)) {
            List<ProductDTO> products = apiService.fetchProducts(
                    searchInput, supermarket, minPrice, maxPrice, null, null
            );
            processProducts(products);
            productSession.setProductList(products);

            // Save search params
            productSession.saveSearchParams(searchInput, supermarket, minPrice, maxPrice);
        }
       
        List<ProductDTO> products = productSession.getProductList();

        // Pagination
        int totalProducts = products.size();
        int fromIndex = currentPage * PRODUCTS_PER_PAGE;
        int toIndex = Math.min(fromIndex + PRODUCTS_PER_PAGE, totalProducts);

        List<ProductDTO> paginatedProducts = products.subList(fromIndex, toIndex);

        boolean hasNextPage = toIndex < totalProducts;
        boolean hasPreviousPage = fromIndex > 0;


        controllerUtils.addUserDataToModel(model);
        model.addAttribute("products", paginatedProducts);
        model.addAttribute("searchQuery", searchInput == null ? "" : searchInput);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("hasNextPage", hasNextPage);
        model.addAttribute("hasPreviousPage", hasPreviousPage);
        model.addAttribute("prevPage", currentPage - 1);
        model.addAttribute("nextPage", currentPage + 1);
        model.addAttribute("title", "SaveX - Productos");

        return "products";
    }
    
    private void processProducts(List<ProductDTO> products) {
        products.forEach(product -> {
            product.setDisplay_name(product.getDisplay_name() != null ? product.getDisplay_name() : "Producto sin nombre");
            product.setThumbnail(product.getThumbnail() != null ? product.getThumbnail() : "backend/src/main/resources/posts/images/post1.png");
            product.setProduct_type(product.getProduct_type() != null ? product.getProduct_type() : "Tipo desconocido");
            product.setProduct_categories(product.getProduct_categories() != null ? product.getProduct_categories() : List.of("Categoría desconocida"));
            product.setPrice(product.getPrice() != null ? product.getPrice() : new PriceDTO());
            product.setSupermarket_name(product.getSupermarket_name() != null ? product.getSupermarket_name() : "Supermercado desconocido");
            product.setProduct_id(product.getProduct_id() != null ? product.getProduct_id() : "ID desconocido");
            product.setProduct_url(product.getProduct_url() != null ? product.getProduct_url() : "#");
            product.set_id(product.get_id() != null ? product.get_id() : "ID desconocido");
        });
    }

    @GetMapping("/products/{supermarket}/{id}")
    public String getProduct(@PathVariable String supermarket, @PathVariable String id, Model model) {
        ProductDTO product = apiService.fetchProduct(supermarket, id);
        

        controllerUtils.addUserDataToModel(model);
        model.addAttribute("product", product);
        
        model.addAttribute("title", "SaveX - " + product.getDisplay_name());
        return "product-detail";
    }


}
