package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.service.ApiService;

@RestController
public class RestProductController {

    @Autowired
    private ApiService apiService;

    @PostMapping("/search") 
    public ResponseEntity<String> search(@RequestParam("searchInput") String searchInput) {
        System.out.println("Search input: " + searchInput);
        System.out.println("---------------------------------------------------");
        String apiResponse = apiService.fetchData();
        System.out.println("---------------------------------------------------");
        System.out.println("API response: " + apiResponse);

        return ResponseEntity.ok(apiResponse);
    }
}
