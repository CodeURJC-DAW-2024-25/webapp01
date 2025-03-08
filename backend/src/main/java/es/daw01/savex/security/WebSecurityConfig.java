package es.daw01.savex.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import es.daw01.savex.model.UserType;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private RepositoryUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
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

                        // Private routes
                        .requestMatchers("/admin").hasAnyRole(UserType.ADMIN.name())
                        .requestMatchers("/createPost").hasAnyRole(UserType.ADMIN.name())
                        .requestMatchers("/editPost/**").hasAnyRole(UserType.ADMIN.name())
                        .requestMatchers("/delete-post/**").hasAnyRole(UserType.ADMIN.name())
                        .requestMatchers("/profile").authenticated()
                        .requestMatchers("/settings").hasAnyRole(UserType.USER.name())
                        .requestMatchers("/api/profile/avatar").authenticated()
                        .requestMatchers("/delete-account").hasAnyRole(UserType.USER.name())
                        .requestMatchers("/shoppingList/**").hasAnyRole(UserType.USER.name(), UserType.ADMIN.name())
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
