package es.daw01.savex.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import es.daw01.savex.model.User;
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
        User user = null;

        // Check if user is authenticated and add data to the model
        if (auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("isAuthenticated", true);

            // Retrieve user data
            user = this.userService.findByUsername(auth.getName()).get();
        } else {
            model.addAttribute("isAuthenticated", false);
        }

        // Set avatar attribute
        if (user == null) {
            model.addAttribute("avatar", DEFAULT_AVATAR);
            model.addAttribute("name", auth.getName());
        } else {
            // Set avatar attribute
            if (user.getAvatar() == null) model.addAttribute("avatar", DEFAULT_AVATAR);
            else model.addAttribute("avatar", user.getAvatar());
            
            // Set name attribute
            if (user.getName().isEmpty()) model.addAttribute("name", user.getUsername());
            else model.addAttribute("name", user.getName());
        }
        
        // Add the rest of the data to the model
        model.addAttribute("username", auth.getName());
        model.addAttribute("role", auth.getAuthorities().stream().findFirst().get().getAuthority());
    }
}
