package es.daw01.savex.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.daw01.savex.model.User;
import es.daw01.savex.model.UserType;
import es.daw01.savex.repository.UserRepository;

@Service
public class RepositoryUserDetailsService implements UserDetailsService{
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        

        List<GrantedAuthority> roles = new ArrayList<>();
        for (UserType role : user.getRoles()) {
            roles.add(new SimpleGrantedAuthority("ROLE_".concat(role.name())));
        }

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getHashedPassword(),
            roles
        );
    }
}
