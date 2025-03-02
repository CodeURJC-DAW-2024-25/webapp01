package es.daw01.savex.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.daw01.savex.model.SupermarketType;

@Service
public class ProductService {
    
    /**
     * Get the available supermarkets for the search form and mark the current one as selected
     * 
     * @param currentSupermarket
     * @return List of supermarkets
    */
    public List<Map<String, Object>> getAvailableSupermarkets(String currentSupermarket) {
        return Arrays.stream(SupermarketType.values())
            .map(s -> Map.of(
                "name", (Object) s.getName(),
                "isSelected", (Object) s.getName().equalsIgnoreCase(currentSupermarket),
                "id", (Object) s.getName().toLowerCase().replace(" ", "")
            ))
            .collect(Collectors.toList());
    }
}
