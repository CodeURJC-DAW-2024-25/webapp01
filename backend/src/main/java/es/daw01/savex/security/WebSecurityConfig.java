package es.daw01.savex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.daw01.savex.model.UserType;
import es.daw01.savex.security.jwt.JwtRequestFilter;
import es.daw01.savex.security.jwt.UnauthorizedHandlerJwt;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private RepositoryUserDetailsService userDetailsService;

    @Autowired
	private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

    @Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/v1/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));
		
		http
			.authorizeHttpRequests(authorize -> authorize
                    // PRIVATE ENDPOINTS
                    //add more endpoints here
					// PUBLIC ENDPOINTS
					.anyRequest().permitAll()
			);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Authentication provider
        http.authenticationProvider(authenticationProvider()); 

        http
                .authorizeHttpRequests(authorize -> authorize
                        // Public routes
                        .requestMatchers("/styles/**", "/scripts/**", "/assets/**", "/favicon.**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/about").permitAll()
                        .requestMatchers("/posts").permitAll()
                        .requestMatchers("/posts/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/products").permitAll()
                        .requestMatchers("/products/**").permitAll()
                        .requestMatchers("/search").permitAll()
                        .requestMatchers("/compare").permitAll()
                        .requestMatchers("/get-compare-table/**").permitAll() 
                        // Private routes
                        .requestMatchers("/admin").hasAnyRole(UserType.ADMIN.name())
                        .requestMatchers("/createPost").hasAnyRole(UserType.ADMIN.name())
                        .requestMatchers("/editPost/**").hasAnyRole(UserType.ADMIN.name())
                        .requestMatchers("/delete-post/**").hasAnyRole(UserType.ADMIN.name())
                        .requestMatchers("/profile").authenticated()
                        .requestMatchers("/settings").hasAnyRole(UserType.USER.name())
                        .requestMatchers("/api/profile/avatar").authenticated()
                        .requestMatchers("/delete-account").hasAnyRole(UserType.USER.name())
                        .requestMatchers("/shoppingList/**").authenticated()
                        .requestMatchers("/update-account-data").authenticated()
                        
                        // Admin
                        .requestMatchers("/admin/**").hasRole(UserType.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/post/**").hasRole(UserType.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/post/**").hasRole(UserType.ADMIN.name())

                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll());

        return http.build();
    }
}
