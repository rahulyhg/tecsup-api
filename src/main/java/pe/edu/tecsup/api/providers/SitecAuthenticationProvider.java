package pe.edu.tecsup.api.providers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.UserService;

import java.util.Collection;

/**
 * Created by ebenites on 23/09/2016.
 */
@Component
public class SitecAuthenticationProvider implements AuthenticationProvider {

    private static Logger log = Logger.getLogger(SitecAuthenticationProvider.class);

    @Autowired
    private UserService userService;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        userService.autenticate(username, password);
        log.info("Login Success!!!");

        User user = userService.loadUserByUsername(username);
        log.info("User: " + user);

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    public boolean supports(Class<?> aClass) {
        return true;
    }
}
