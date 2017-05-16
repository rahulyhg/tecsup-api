package pe.edu.tecsup.api.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.UserService;
import pe.edu.tecsup.api.utils.Constant;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;

@Controller
public class AuthController {

	private static final Logger log = Logger.getLogger(AuthController.class);

    @Autowired
    private HttpSession session;

    @Autowired
    private UserService userService;

    @Value("${gapi.clientid}")
    private String GAPI_CLIENT_ID;

    // Api Backend: https://developers.google.com/identity/sign-in/web/backend-auth

    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();

	@PostMapping("/connect")
	private ResponseEntity<String> connect(@RequestParam String token) throws Exception{
		log.info("connect() token:" + token);
		try {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(Collections.singletonList(GAPI_CLIENT_ID)).build();
            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null)
                throw new Exception("Token invalido, intentar nuevamente o contactar con el administrador.");

            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            log.debug("ID: " + payload.getSubject());
            log.debug("Name: " + payload.get("name"));
            log.debug("Email: " + payload.getEmail());
            log.debug("Image URL: " + payload.get("picture"));

            String email = payload.getEmail();
            // boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());

            // Verificar si es del dominio TECSUP
            String usuario = email.substring(0, email.indexOf("@")).toLowerCase();
            String dominio = email.substring(email.indexOf("@")+1).toLowerCase();
            log.info("usuario:" + usuario + " - dominio:" + dominio);

            if (!Arrays.asList(Constant.GOOGLEPLUS_ALLOW_DOMAINS).contains(dominio))
                throw new Exception("La cuenta de correo " + email + " NO corresponde a la lista de dominios permitidos "+Arrays.toString(Constant.GOOGLEPLUS_ALLOW_DOMAINS)+".");

            log.info("Cuenta " + usuario + " pertenece a la lista blanca " + Arrays.toString(Constant.GOOGLEPLUS_ALLOW_DOMAINS));

            // Verificando si se encuentra registrado el usuario
            User user = userService.loadUserByUsername(usuario);
            log.info(user);

            // Success !!

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());

            log.info("Ingreso satisfactorio con Google!: " + authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return new ResponseEntity<>("Ingreso satisfactorio", HttpStatus.OK);

		} catch (Throwable e) {
			log.error(e, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
