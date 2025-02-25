package es.daw01.savex.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiService {
    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchData(String searchInput) {
        String apiUrl = "https://market-pricings-server.vercel.app/api/v1/markets/mercadona?product_name=" + searchInput;
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        return response.getBody();
    }
}
