package es.daw01.savex.controller.api.v1;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
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

@RestController
@RequestMapping("/api")
public class RestUserController {

    private final static String DEFAULT_AVATAR_PATH = "static/assets/defaultAvatar.jpg";

    @Autowired
    private UserService userService;

    @Autowired
    private ControllerUtils controllerUtils;

    @GetMapping("/profile/{id}/avatar")
    public ResponseEntity<Object> getProfilePic(@PathVariable long id) throws SQLException {
        Blob avatar = null;

        User user = userService.findById(id);

        // If the user does not exist or the avatar is null, return default avatar

        if (user == null || user.getAvatar() == null) {
            Resource img = new ClassPathResource(DEFAULT_AVATAR_PATH);
            try {
                avatar = BlobProxy.generateProxy(img.getInputStream(), img.contentLength());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentLength(avatar.length())
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .body(new InputStreamResource(avatar.getBinaryStream()));
        }

        // Get the user avatar if it exists and return it
        avatar = user.getAvatar();
        Resource resource = new InputStreamResource(avatar.getBinaryStream());

        return ResponseEntity.ok()
                .contentLength(avatar.length())
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(resource);
    }

    @PostMapping("/profile/avatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(@RequestParam MultipartFile avatar) {
        User user = controllerUtils.getAuthenticatedUser();

        try {
            userService.updateUserAvatar(user.getUsername(), avatar);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    Map.of("message", e.getMessage()));
        }
        return ResponseEntity.ok().body(
                Map.of("message", "Avatar updated successfully"));
    }
}