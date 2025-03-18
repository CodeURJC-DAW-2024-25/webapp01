package es.daw01.savex.controller.api.v1;

import java.beans.Transient;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;
import java.util.NoSuchElementException;

import jakarta.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {


    @Autowired
    private UserService userService;

    @Autowired
    private ControllerUtils controllerUtils; //TODO check if this is needed

    @GetMapping({"", "/"})
    public ResponseEntity<Object> getUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        try{
            // Get all users
            Map<String, Object> response = userService.findAllByRole(
                UserType.USER,
                PageRequest.of(page, size)
            );

            // Return the users list
            return ApiResponseDTO.ok(response);
        }
        catch (Exception e){
            // Return error message
            return ApiResponseDTO.error("Error getting users");    
        }
    }

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
        User user = controllerUtils.getAuthenticatedUser();
        if (user.getId() != id) {
            return ResponseEntity.status(403).build();
        }
        URI location = fromCurrentRequest().build().toUri();
        try{
        userService.createUserAvatar(id, location, avatar);
        }catch(EntityExistsException e) {
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}/avatar")
    public ResponseEntity<Map<String, Object>> modifyAvatar(
        @PathVariable long id, @RequestParam MultipartFile avatar) throws IOException {
        User user = controllerUtils.getAuthenticatedUser();
        if (user.getId() != id) {
            return ResponseEntity.status(403).build();
        }
        URI location = fromCurrentRequest().build().toUri();
        try{
            userService.modifyUserAvatar(id, location, avatar);
        }catch(NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<Object> deleteAvatar(@PathVariable long id) {
        User user = controllerUtils.getAuthenticatedUser();
        if (user.getId() != id) {
            return ResponseEntity.status(403).build();
        }
        try{
            userService.deleteUserAvatar(id);
        }catch(NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        User user = controllerUtils.getAuthenticatedUser();
        if (user.getId() != id && !(user.getRole() == UserType.ADMIN)) {
            return ResponseEntity.status(403).build();
        }

        try{
            userService.deleteById(id);
        }catch(NoSuchElementException e) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok().build();
    }

}