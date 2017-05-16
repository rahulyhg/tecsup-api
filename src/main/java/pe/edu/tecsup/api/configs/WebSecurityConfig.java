package pe.edu.tecsup.api.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.edu.tecsup.api.providers.SitecAuthenticationProvider;
import pe.edu.tecsup.api.filters.JwtAuthenticationTokenFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SitecAuthenticationProvider sitecAuthenticationProvider;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(sitecAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/" /*, "/**"*/).permitAll()
                .antMatchers("/home/**").authenticated()
                .and()
                    .formLogin().loginPage("/login").loginProcessingUrl("/authenticate").defaultSuccessUrl("/home/").failureUrl("/login?error").usernameParameter("username").passwordParameter("password")
                .and()
                    .logout().logoutUrl("/logout").logoutSuccessUrl("/")
                .and()
                    .csrf().disable();

        // Custom JWT based security filter: https://github.com/szerhusenBC/jwt-spring-security-demo
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter("/api/**");
    }

}