package es.daw01.savex.components;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.daw01.savex.model.Post;
import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.model.VisibilityType;
import es.daw01.savex.repository.PostRepository;
import es.daw01.savex.repository.UserRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void init() {
        this.initUsers();
        this.initPosts();
    }

    /**
     * Load default users into the database
    */
    private void initUsers() {
        // Load default user
        if (
            !userRepository.existsByUsername("userDefault") &&
            !userRepository.existsByEmail("userEmail@gmail.com")
        ) {
            userRepository.save(
                new User(
                    "userEmail@gmail.com",
                    "userDefault",
                    "User",
                    passwordEncoder.encode("pass"),
                    null,
                    UserType.USER
                )
            );
        }

        // Load default admin
        if (
            !userRepository.existsByUsername("adminDefault") &&
            !userRepository.existsByEmail("adminEmail@gmail.com")
        ) {
            userRepository.save(
                new User(
                    "adminEmail@gmail.com",
                    "adminDefault",
                    "Admin",
                    passwordEncoder.encode("admin"),
                    null,
                    UserType.ADMIN
                )
            );
        }
    }

    /**
     * Load default posts into the database
    */
    private void initPosts() {
        // Load default post
        if (!postRepository.existsByTitle("Default Post")) {
            postRepository.save(
                new Post(
                    "Cómo usar nuestro comparador de precios",
                    "This is a default post description",
                    """
                    # Cómo usar nuestro comparador de precios

                    **Descripción:** 
                    En este post te enseñaremos cómo utilizar nuestro comparador de precios para encontrar las mejores ofertas.

                    **Contenido:**
                    Para usar nuestro comparador de precios, sigue estos pasos:
                    1. Ingresa a nuestra página web.
                    2. En la barra de búsqueda, escribe el nombre del producto que deseas comparar.
                    3. Haz clic en el botón de búsqueda.
                    4. Se mostrarán los resultados de la búsqueda con los precios de diferentes tiendas.
                    5. Utiliza los filtros para ajustar los resultados según tus preferencias (precio, tienda, etc.).
                    6. Haz clic en el producto que te interese para ver más detalles y comparar precios.
                    7. ¡Listo! Ahora sabes cómo usar nuestro comparador de precios para encontrar las mejores ofertas.

                    **Autor:** admin

                    **Fecha:** 17 de febrero de 2025

                    **Visibilidad:** Público

                    **Etiquetas:** 
                    - Comparador
                    - Precios
                    - Tutorial

                    **Banner:**
                    ![Comparador de precios](assets/bannerpost1.png)
                    """,
                    "saveX",
                    "2025-02-17",
                    VisibilityType.PUBLIC,
                    Arrays.asList("default", "post"),
                    null
                )
            );
        }
    }
}
