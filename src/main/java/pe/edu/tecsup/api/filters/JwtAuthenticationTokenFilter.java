package pe.edu.tecsup.api.filters;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.UserService;
import pe.edu.tecsup.api.services.JwtTokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * https://github.com/szerhusenBC/jwt-spring-security-demo
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final Logger log = Logger.getLogger(JwtAuthenticationTokenFilter.class);

    private RequestMatcher requestMatcher;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService jwtTokenService;

    public JwtAuthenticationTokenFilter(String pattrern) {
        this.requestMatcher = new AntPathRequestMatcher(pattrern);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if(requestMatcher.matches(request)) {

            log.info("Filter API:" + request.getRequestURI());

            String authToken = request.getHeader(JwtTokenService.HEADER_AUTHORIZATION);
            log.info("authToken " + authToken);

            if(authToken != null) {

                String username = jwtTokenService.parseToken(authToken, true);
                log.info("checking authentication for user " + username);

                // Update lastdate token
                try {
                    userService.updateToken(authToken);
                }catch (Exception e){
                    log.error(e, e);
                }

                User user = userService.loadUserByUsername(username);
                log.info("user " + user);

                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    log.error("Principal Before" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                }

                log.info("authenticated user " + username + ", setting security context");
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                    log.error("Principal After" + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                }

            }

        }

        chain.doFilter(request, response);
    }
}