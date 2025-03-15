package es.daw01.savex.security.jwt;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class UnauthorizedHandlerJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(UnauthorizedHandlerJwt.class);

	@Override
	public void commence(
		HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException
	) throws IOException {
		logger.info("Unauthorized error: {}", authException.getMessage());

		String errorMessage = String.format(
			"{\"status\": %d, \"error\": \"Unauthorized\", \"message\": \"%s\", \"path\": \"%s\"}",
			HttpServletResponse.SC_UNAUTHORIZED,
			authException.getMessage(),
			request.getServletPath()
		);
		
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(errorMessage);
	}
}
