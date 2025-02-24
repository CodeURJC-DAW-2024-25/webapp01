package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;

  
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.service.ApiService;

@Controller
public class ProductsController {

    @Autowired
    private ControllerUtils controllerUtils;

     @Autowired
    private ApiService apiService;

      
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/search") 
    public String search(@RequestParam("searchInput") String searchInput, Model model) {
        controllerUtils.addUserDataToModel(model);
        model.addAttribute("title", "SaveX - Productos");
        model.addAttribute("searchQuery", searchInput);
     
        //Catch the JSON response from the API
        String productsJSON = apiService.fetchData(searchInput);

        try {
            //Convert the JSON response to a List of Maps
            List<Map<String, Object>> products = objectMapper.readValue(productsJSON, new TypeReference<List<Map<String, Object>>>() {});
            model.addAttribute("products", products);
        } catch (Exception e) {
            model.addAttribute("products", List.of()); 
        }

        return "products"; 
    }
}
