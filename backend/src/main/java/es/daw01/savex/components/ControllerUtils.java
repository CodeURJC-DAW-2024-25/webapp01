package es.daw01.savex.components;

import java.sql.Blob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import es.daw01.savex.service.UserService;

@Component
public class ControllerUtils {

    private static final String DEFAULT_AVATAR = "/assets/defaultAvatar.svg";

    @Autowired
    private UserService userService;

    /**
     * Add authenticated user data to the model
     * @param model Model to add the data
    */
    public void addUserDataToModel(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Blob avatar = null;

        // Check if user is authenticated and add data to the model
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("isAuthenticated", true);

            // Retrieve user avatar
            avatar = this.userService.findByUsername(auth.getName()).get().getAvatar();
        } else {
            model.addAttribute("isAuthenticated", false);
        }

        // Check for null avatar
        if (avatar == null) model.addAttribute("avatar", DEFAULT_AVATAR);
        else model.addAttribute("avatar", avatar);
        
        // Add the rest of the data to the model
        model.addAttribute("username", auth.getName());
        model.addAttribute("role", auth.getAuthorities().stream().findFirst().get().getAuthority());
    }
}
