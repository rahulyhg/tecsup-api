package pe.edu.tecsup.api.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by ebenites on 24/03/2017.
 */
@Configuration
@EnableScheduling
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers (ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/home/");
        registry.addViewController("/login").setViewName("login"); // setStatusCode(HttpStatus.OK);
    }

}
