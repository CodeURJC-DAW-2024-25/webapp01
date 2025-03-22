package es.daw01.savex.controller.api.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw01.savex.DTOs.ApiResponseDTO;
import es.daw01.savex.DTOs.users.CreateUserRequest;
import es.daw01.savex.DTOs.users.ModifyUserPassword;
import es.daw01.savex.DTOs.users.PrivateUserDTO;
import es.daw01.savex.model.User;
import es.daw01.savex.security.jwt.AuthResponse;
import es.daw01.savex.security.jwt.AuthResponse.Status;
import es.daw01.savex.security.jwt.LoginRequest;
import es.daw01.savex.security.jwt.UserLoginService;
import es.daw01.savex.service.UserService;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class RestLoginController {
	
	@Autowired
	private UserLoginService userLoginService;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
		@RequestBody LoginRequest loginRequest,
		HttpServletResponse response
	) {
		return userLoginService.login(response, loginRequest);
	}

	@PostMapping("/register")
    public ResponseEntity<Object> register(@ModelAttribute CreateUserRequest createUserRequest) {
        
        Map<String, String> errors = new HashMap<>();
        
        try {
            // register new user
            PrivateUserDTO privateUserDTO = userService.register(createUserRequest, errors);

            // Return success message
            return ApiResponseDTO.ok(privateUserDTO);
        } catch (NoSuchElementException e) {
            // Return error message
            return ApiResponseDTO.error("User not found");
        } catch (Exception e) {
            // Return error message
            return ApiResponseDTO.error(errors.toString());
        }
    }

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(
		@CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response
	) {
		return userLoginService.refresh(response, refreshToken);
	}

	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
		return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userLoginService.logout(response)));
	}
}