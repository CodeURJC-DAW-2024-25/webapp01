package es.daw01.savex.controller.api.v1;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.User;
import es.daw01.savex.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {


    @Autowired
    private UserService userService;

    @Autowired
    private ControllerUtils controllerUtils; //TODO check if this is needed

    @GetMapping("/{id}/avatar")
    public ResponseEntity<Object> getProfilePic(@PathVariable long id) throws SQLException, IOException {
        Resource avatar = userService.getUserAvatar(id);
        return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_TYPE, "image/png")
        .body(avatar);
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(
        @PathVariable long id, @RequestParam MultipartFile avatar) throws IOException {
        User user = controllerUtils.getAuthenticatedUser(); // TODO check if this is needed
        if (user.getId() != id) {
            return ResponseEntity.status(403).build();
        }
        URI location = fromCurrentRequest().build().toUri();
        userService.createPostImage(id, location, avatar);
        return ResponseEntity.created(location).build();
    }
}