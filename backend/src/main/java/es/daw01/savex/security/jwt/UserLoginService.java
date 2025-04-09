package es.daw01.savex.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import es.daw01.savex.DTOs.users.UserMapper;
import es.daw01.savex.model.User;
import es.daw01.savex.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserLoginService {

	private static final Logger log = LoggerFactory.getLogger(UserLoginService.class);

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;
	private final UserMapper userMapper;

	public UserLoginService(
		AuthenticationManager authenticationManager,
		UserDetailsService userDetailsService,
		JwtTokenProvider jwtTokenProvider,
		UserService userService,
		UserMapper userMapper
	) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userService = userService;
		this.userMapper = userMapper;
	}

	public ResponseEntity<AuthResponse> login(HttpServletResponse response, LoginRequest loginRequest) {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		
		String username = loginRequest.getUsername();
		UserDetails user = userDetailsService.loadUserByUsername(username);
		User userEntity = userService.findByUsername(username).get();

		HttpHeaders responseHeaders = new HttpHeaders();
		var newAccessToken = jwtTokenProvider.generateAccessToken(user);
		var newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

		response.addCookie(buildTokenCookie(TokenType.ACCESS, newAccessToken));
		response.addCookie(buildTokenCookie(TokenType.REFRESH, newRefreshToken));

		AuthResponse loginResponse = new AuthResponse(
			AuthResponse.Status.SUCCESS,
			"Auth successful. Tokens are created in cookie.",
			null,
			userMapper.toPublicUserDTO(userEntity),
			true
		);

		return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
	}

	public ResponseEntity<AuthResponse> refresh(HttpServletResponse response, String refreshToken) {
		try {
			var claims = jwtTokenProvider.validateToken(refreshToken);
			UserDetails user = userDetailsService.loadUserByUsername(claims.getSubject());
			User userEntity = userService.findByUsername(claims.getSubject()).get();

			var newAccessToken = jwtTokenProvider.generateAccessToken(user);
			response.addCookie(buildTokenCookie(TokenType.ACCESS, newAccessToken));

			AuthResponse loginResponse = new AuthResponse(
				AuthResponse.Status.SUCCESS,
				"Auth successful. Tokens are created in cookie.",
				null,
				userMapper.toPublicUserDTO(userEntity),
				true
			);

			return ResponseEntity.ok().body(loginResponse);

		} catch (Exception e) {
			log.error("Error while processing refresh token", e);
			AuthResponse loginResponse = new AuthResponse(
				AuthResponse.Status.FAILURE,
				null,
				"Failure while processing refresh token",
				null,
				false
			);

			return ResponseEntity.ok().body(loginResponse);
		}
	}

	public String logout(HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		response.addCookie(removeTokenCookie(TokenType.ACCESS));
		response.addCookie(removeTokenCookie(TokenType.REFRESH));

		return "logout successfully";
	}

	public ResponseEntity<AuthResponse> checkSession(String accessToken, String refreshToken) {
		if (accessToken == null && refreshToken == null) {
			return ResponseEntity.ok(new AuthResponse(
				AuthResponse.Status.FAILURE,
				"Session expired",
				null,
				null,
				false
			));
		}
		
		try {
			var claims = jwtTokenProvider.validateToken(accessToken);
			if (claims == null) claims = jwtTokenProvider.validateToken(refreshToken);

			if (claims == null) {
				return ResponseEntity.ok(new AuthResponse(
					AuthResponse.Status.FAILURE,
					"Session expired",
					null,
					null,
					false
				));
			}

			User user = userService.findByUsername(claims.getSubject()).get();

			AuthResponse response = new AuthResponse(
				AuthResponse.Status.SUCCESS,
				"Session is valid",
				null,
				userMapper.toPublicUserDTO(user),
				true
			);

			return ResponseEntity.ok().body(response);

		} catch (Exception e) {
			log.error("Error while checking session", e);
			return ResponseEntity.status(500).body(new AuthResponse(AuthResponse.Status.FAILURE, "An error occurred while checking the session"));
		}
	}

	private Cookie buildTokenCookie(TokenType type, String token) {
		Cookie cookie = new Cookie(type.cookieName, token);
		cookie.setMaxAge((int) type.duration.getSeconds());
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setAttribute("SameSite", "None");
		return cookie;
	}

	private Cookie removeTokenCookie(TokenType type){
		Cookie cookie = new Cookie(type.cookieName, "");
		cookie.setMaxAge(0);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setAttribute("SameSite", "None");
		return cookie;
	}
}
