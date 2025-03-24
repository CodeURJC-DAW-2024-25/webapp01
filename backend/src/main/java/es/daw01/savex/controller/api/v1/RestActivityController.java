package es.daw01.savex.controller.api.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.HashMap;


@RestController
@RequestMapping("/api/v1/activity")
public class RestActivityController {
    
    @GetMapping("/data")
    public Map<String, Object> getActivityData() {
        Map<String, Object> response = new HashMap<>();
        response.put("labels", new String[]{"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"});
        response.put("data", new int[]{1000, 1200, 900, 1500, 2000, 1800, 2500, 1900, 1300, 1700, 2200, 2500});
        return response;
    }

    

}
