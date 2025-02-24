package es.daw01.savex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

import es.daw01.savex.service.ApiService;

@Controller
public class RestProductController {
    @Autowired
    private ApiService apiService;

    @PostMapping("/search") 
    public String search(@RequestParam("searchInput") String searchInput, Model model) {
        System.out.println("Search input: " + searchInput);
        System.out.println("---------------------------------------------------");
        String apiResponse = apiService.fetchData();
        System.out.println("---------------------------------------------------");
        System.out.println("API response: " + apiResponse);
        model.addAttribute("apiResponse", apiResponse);
        return "redirect:/";
    }
}
