package es.daw01.savex.controller.api.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import es.daw01.savex.security.jwt.AuthResponse;
import es.daw01.savex.security.jwt.AuthResponse.Status;
import es.daw01.savex.security.jwt.LoginRequest;
import es.daw01.savex.security.jwt.UserLoginService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class RestLoginController {
	
	@Autowired
	private UserLoginService userLoginService;

	@PostMapping("/login")
	@CrossOrigin(origins = "*", allowCredentials = "true")
	public ResponseEntity<AuthResponse> login(
		@RequestBody LoginRequest loginRequest,
		HttpServletResponse response
	) {
		return userLoginService.login(response, loginRequest);
	}

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(
		@CookieValue(name = "RefreshToken", required = false) String refreshToken, HttpServletResponse response
	) {
		return userLoginService.refresh(response, refreshToken);
	}

	@PostMapping("/logout")
	@CrossOrigin(origins = "*", allowCredentials = "true")
	public ResponseEntity<AuthResponse> logOut(HttpServletResponse response) {
		return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userLoginService.logout(response)));
	}
}