package es.daw01.savex.controller.api.v1;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import jakarta.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.PaginatedDTO;
import es.daw01.savex.DTOs.users.CreateUserRequest;
import es.daw01.savex.DTOs.users.ModifyPasswordRequest;
import es.daw01.savex.DTOs.users.ModifyUserRequest;
import es.daw01.savex.DTOs.users.PrivateUserDTO;
import es.daw01.savex.DTOs.users.PublicUserDTO;
import es.daw01.savex.components.ControllerUtils;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.service.CommentService;
import es.daw01.savex.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;


@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ControllerUtils controllerUtils;

    @Operation(summary = "Get all users Paginated")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Users list returned successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaginatedDTO.class)
                )
            }),
        @ApiResponse( 
            responseCode = "500", 
            description = "Internal server error",
            content = @Content
        )
    })
    @GetMapping({ "", "/" })
    public ResponseEntity<Object> getUsers(
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        try{
            // Get all users
            PaginatedDTO<PublicUserDTO> response = userService.findAllByRoleNoPasswd(
                UserType.USER,
                pageable
            );
            // Return the users list
            return ApiResponseDTO.ok(response);
        } catch (IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage());
        }
    }

    @Operation(summary = "Get logged user avatar")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Avatar returned successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Resource.class)
                )
            }),
        @ApiResponse(
            responseCode = "404", 
            description = "Resource not found",
            content = @Content
        )
    })
    @GetMapping("/{id}/avatar")
    public ResponseEntity<Object> getProfilePic(@PathVariable long id) throws SQLException, IOException {
        Resource avatar = userService.getUserAvatar(id);
        return ApiResponseDTO.ok(avatar);
    }


    @Operation(summary = "Upload avatar for logged user")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Avatar uploaded successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PublicUserDTO.class)
                )
            }),
        @ApiResponse(
            responseCode = "409", 
            description = "Avatar already exists",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "You cannot upload an avatar for another user",
            content = @Content
        )
    })
    @PostMapping("/{id}/avatar")
    public ResponseEntity<Object> uploadAvatar( @PathVariable long id, @RequestParam MultipartFile avatar) throws IOException {
        User user = controllerUtils.getAuthenticatedUser();
        if (user.getId() != id) {
            return ApiResponseDTO.error("You cannot upload an avatar for another user", 403);
        }

        URI location = fromCurrentRequest().build().toUri();
        
        try {
            PublicUserDTO updatedUser = userService.createUserAvatar(id, avatar);
            return ApiResponseDTO.ok(updatedUser, location, 201);
        } catch (EntityExistsException e) {
            return ApiResponseDTO.error("Avatar already exists", 409);
        }
    }


    @Operation(summary = "Modify avatar for logged user")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Avatar modified successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PublicUserDTO.class)
                )
            }),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "You cannot modify an avatar for another user",
            content = @Content
        )
    })
    @PutMapping("/{id}/avatar")
    public ResponseEntity<Object> modifyAvatar(
            @PathVariable long id, @RequestParam MultipartFile avatar) throws IOException {
        User user = controllerUtils.getAuthenticatedUser();
        if (user.getId() != id) {
            return ApiResponseDTO.error("You cannot modify an avatar for another user", 403);
        }
        URI location = fromCurrentRequest().build().toUri();
        try {
            PublicUserDTO updatedUser = userService.modifyUserAvatar(id, avatar);
            return ApiResponseDTO.ok(updatedUser, location, 200);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error(e.getMessage(), 404);
        }
    }

    @Operation(summary = "Delete avatar for logged user")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Avatar deleted successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class)
                )
            }),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content
            ),
        @ApiResponse(
            responseCode = "403", 
            description = "You cannot delete an avatar for another user",
            content = @Content
            )
    })
    @DeleteMapping("/{id}/avatar")
    public ResponseEntity<Object> deleteAvatar(@PathVariable long id) {
        User user = controllerUtils.getAuthenticatedUser();
        if (user.getId() != id) {
            return ApiResponseDTO.error("You cannot delete an avatar for another user", 403);
        }
        try {
            userService.deleteUserAvatar(id);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("User not found", 404);
        }
        return ApiResponseDTO.ok("Avatar deleted successfully");
    }

    @Operation(summary = "Delete user")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User deleted successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class)
                )
            }),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content
            ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error deleting user",
            content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        try {
            // Delete user
            commentService.deleteByAuthorId(id);
            userService.deleteById(id);

            return ApiResponseDTO.ok("User deleted successfully");
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("User not found");
        } catch (IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponseDTO.error("Error deleting user");
        }
    }

    @Operation(summary = "Register new user")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "201", 
            description = "User created successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PrivateUserDTO.class)
                )
            }),
        @ApiResponse(
            responseCode = "409", 
            description = "User already exists",
            content = @Content
            ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error creating user",
            content = @Content
            )
    })
    @PostMapping({"", "/"})
    public ResponseEntity<Object> registerNewUser(@RequestBody CreateUserRequest createUserRequest) {
        try {
            System.out.println(createUserRequest);
            PrivateUserDTO privateUser = userService.register(createUserRequest);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(privateUser.id()).toUri();
            return ApiResponseDTO.ok(privateUser, location, 201);
        } catch (EntityExistsException e) {
            return ApiResponseDTO.error("User already exists",409);
        } catch (IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponseDTO.error("Error creating user");
        }
    }

    @Operation(summary = "Get user by id")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User returned successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PublicUserDTO.class)
                )
            }),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable long id) {
        try {
            // Get the user
            PublicUserDTO user = userService.findPublicUserById(id);

            // Return the user
            return ApiResponseDTO.ok(user);
        } catch (NoSuchElementException e) {
            // Return error message
            return ApiResponseDTO.error("User not found");
        }
    }
    
    @Operation(summary = "Modify user")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User modified successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PrivateUserDTO.class)
                )
            }),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "You cannot modify another user",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error modifying user",
            content = @Content
        )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Object> modifyUser(
        @PathVariable long id,
        @RequestBody ModifyUserRequest modifyUser
    ) {
        try {
            User authenticatedUser = controllerUtils.getAuthenticatedUser();

            if (authenticatedUser.getId() != id) {
                return ApiResponseDTO.error("You cannot modify another user", 403);
            }
            
            PrivateUserDTO privateUser = userService.modifyUser(id, modifyUser);

            return ApiResponseDTO.ok(privateUser);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("User not found");
        } catch (IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponseDTO.error("Error modifying user");
        }
    }

    @Operation(summary = "Modify user password")
    @ApiResponses( value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Password modified successfully",
            content = {
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PrivateUserDTO.class)
                )
            }),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "You cannot modify another user",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error modifying password",
            content = @Content
        )
    })
    @PatchMapping("/{id}/password")
    public ResponseEntity<Object> modifyPassword(
        @PathVariable long id,
        @RequestBody ModifyPasswordRequest modifyUserPassword
    ) {
        try {
            // Get the authenticated user
            User authenticatedUser = controllerUtils.getAuthenticatedUser();

            // Prevent user from modifying another user
            if (authenticatedUser.getId() != id) {
                return ApiResponseDTO.error("You cannot modify another user", 403);
            }

            // Modify the password
            PrivateUserDTO privateUserDTO = userService.modifyPassword(id, modifyUserPassword);

            return ApiResponseDTO.ok(privateUserDTO);
        } catch (NoSuchElementException e) {
            return ApiResponseDTO.error("User not found");
        } catch (IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage());
        } catch (Exception e) {
            return ApiResponseDTO.error("Error modifying password");
        }
    }

    @GetMapping("/{id}/email")
    public ResponseEntity<Object> getUserEmail(@PathVariable long id) {
        try {
            // Get the user
            String email = userService.getUserEmail(id);
            // Return the user
            return ApiResponseDTO.ok(email);
            
        }catch (IllegalArgumentException e) {
            return ApiResponseDTO.error(e.getMessage(), 401);

        }catch (NoSuchElementException e) {
            // Return error message
            return ApiResponseDTO.error("User not found");

        } 

    }

}