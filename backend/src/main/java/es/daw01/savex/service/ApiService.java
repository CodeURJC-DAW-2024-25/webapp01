package es.daw01.savex.service;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiService {
    private final RestTemplate restTemplate = new RestTemplate();


       public String fetchData() {
        String url = "https://market-pricings-server.vercel.app/";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }
}
