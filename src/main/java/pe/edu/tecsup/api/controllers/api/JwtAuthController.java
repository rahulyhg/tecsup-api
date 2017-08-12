package pe.edu.tecsup.api.controllers.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.jsonwebtoken.JwtException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.JwtTokenService;
import pe.edu.tecsup.api.services.UserService;
import pe.edu.tecsup.api.utils.Constant;

import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/jwt")
public class JwtAuthController {

    private static final Logger log = Logger.getLogger(JwtAuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @PostMapping("access_token")
    public ResponseEntity<?> createAuthenticationToken(@RequestParam String username, @RequestParam String password) throws Exception {
        log.info("call createAuthenticationToken: "+username+"-"+password);
        try {

            // Attempt to verify the credentials and pass or throws BadCredentialsException
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Get User data from database
            User user = userService.loadUserByUsername(username);
            log.info("User: " + user);

            // Generate Token from User
            String token = jwtTokenService.generateToken(user.getUsername());
            log.info("Token: " + token);
            user.setToken(token);

            // Return the token
            return ResponseEntity.ok(user);

        }catch (UsernameNotFoundException e){
            log.error(e, e);
            throw new Exception(e.getMessage());
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

//    @GetMapping("refresh_token")
//    public ResponseEntity<?> refreshAndGetAuthenticationToken(@RequestParam String token) throws Exception{
//        log.info("call refreshAndGetAuthenticationToken: "+token);
//        try {
//            String username = jwtTokenService.parseToken(token);
//            log.info("Token: " + token);
//
//            User user = userService.loadUserByUsername(username);
//            log.info("User: " + user);
//
//            String refreshedToken = jwtTokenService.refreshToken(token);
//            log.info("Refreshed Token: " + refreshedToken);
//            user.setToken(refreshedToken);
//
//            //userService.refreshToken(token, refreshedToken);
//
//            return ResponseEntity.ok(user);
//
//        }catch (UsernameNotFoundException e){
//            log.error(e, e);
//            throw new Exception(e.getMessage());
//        }catch (Throwable e){
//            log.error(e, e);
//            throw e;
//        }
//    }

    /**
     * Google Authentication Token
     */

    @Value("${gapi.clientid}")
    private String GAPI_CLIENT_ID;

    // Api Backend: https://developers.google.com/identity/sign-in/web/backend-auth

    private static final HttpTransport transport = new NetHttpTransport();
    private static final JsonFactory jsonFactory = new JacksonFactory();


    @PostMapping("google_access_token")
    public ResponseEntity<?> createGoogleAuthenticationToken(@RequestParam String id_token, @RequestParam String instanceid,
                                                             @RequestParam(required = false) String deviceid, @RequestParam(required = false) String manufacturer, @RequestParam(required = false) String model, @RequestParam(required = false) String device, @RequestParam(required = false) String kernel, @RequestParam(required = false) String version, @RequestParam(required = false) Integer sdk) throws Exception {
        log.info("call createGoogleAuthenticationToken: google_id_token:"+id_token+" - instance:"+instanceid+" - deviceid:"+deviceid+" - manufacturer:"+manufacturer+" - model:"+model+" - device:"+device+" - kernel:"+kernel+" - version:"+version+" - sdk:"+sdk);
        try {

            // Attempt to verify the Google Token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).setAudience(Collections.singletonList(GAPI_CLIENT_ID)).build();

            GoogleIdToken idToken;
            try {
                idToken = verifier.verify(id_token);
            }catch(IllegalArgumentException e){
                throw new Exception("No es un Google Token ID valido", e);
            }

            if (idToken == null)
                throw new Exception("Google Token ID incorrexto");

            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            log.debug("User ID: " + userId);

            // Get profile information from payload
            log.debug("ID: " + payload.getSubject());
            log.debug("Name: " + payload.get("name"));
            log.debug("Email: " + payload.getEmail());
            log.debug("Image URL: " + payload.get("picture"));

            String email = payload.getEmail();
            // boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());

            // Verificar si es del dominio TECSUP
            String usuario = email.substring(0, email.indexOf("@")).toLowerCase();
            String dominio = email.substring(email.indexOf("@") + 1).toLowerCase();
            log.info("usuario:" + usuario + " - dominio:" + dominio);

            if (!Arrays.asList(Constant.GOOGLEPLUS_ALLOW_DOMAINS).contains(dominio))
                throw new Exception("La cuenta de correo " + email + " NO corresponde a la lista de dominios permitidos " + Arrays.toString(Constant.GOOGLEPLUS_ALLOW_DOMAINS) + ".");

            log.info("Cuenta " + usuario + " pertenece a la lista blanca " + Arrays.toString(Constant.GOOGLEPLUS_ALLOW_DOMAINS));

            // Verificando si se encuentra registrado el usuario
            User user = userService.loadUserByUsername(usuario);
            log.info(user);

            // Gmail info
            user.setGid(payload.getSubject());
            user.setName((String) payload.get("name"));
            user.setEmail(payload.getEmail());
            user.setPicture((String) payload.get("picture"));

            // Generate Token from User
            String token = jwtTokenService.generateToken(user.getUsername());
            log.info("Token: " + token);
            user.setToken(token);

            // Updating instance in db
            userService.saveAccess(user.getId(), instanceid, token, deviceid, manufacturer, model, device, kernel, version, sdk);

            log.info(user);

            // Return the token
            return ResponseEntity.ok(user);

        }catch (UsernameNotFoundException e){
            log.error(e, e);
            throw new Exception(e.getMessage());
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @PostMapping("hacked_access_token")
    public ResponseEntity<?> createHackedAuthenticationToken(@RequestParam String username, @RequestParam String id_token, @RequestParam String instanceid,
                                                             @RequestParam(required = false) String deviceid, @RequestParam(required = false) String manufacturer, @RequestParam(required = false) String model, @RequestParam(required = false) String device, @RequestParam(required = false) String kernel, @RequestParam(required = false) String version, @RequestParam(required = false) Integer sdk) throws Exception {
        log.info("call createHackedAuthenticationToken: username:"+username+" - gtoken:"+id_token+" - instance:"+instanceid+" - deviceid:"+deviceid+" - manufacturer:"+manufacturer+" - model:"+model+" - device:"+device+" - kernel:"+kernel+" - version:"+version+" - sdk:"+sdk);
        try {

            // Attempt to verify the Google Token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory).build();

            GoogleIdToken idToken;
            try {
                idToken = verifier.verify(id_token);
            } catch (IllegalArgumentException e) {
                throw new Exception("No es un Google Token ID valido", e);
            }

            if (idToken == null)
                throw new Exception("Google Token ID incorrecto");

            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            log.debug("User ID: " + userId);

            // Get profile information from payload
            log.debug("ID: " + payload.getSubject());
            log.debug("Name: " + payload.get("name"));
            log.debug("Email: " + payload.getEmail());
            log.debug("Image URL: " + payload.get("picture"));

            // Validate Admin!!!
            userService.validateAdmin(payload.getEmail());

            // Verificando si se encuentra registrado el usuario
            User user = userService.loadUserByUsername(username);
            log.info(user);

            // Gmail info
            user.setGid(payload.getSubject());
            user.setName(user.getFullname());
            user.setEmail(user.getEmail());
            user.setPicture((payload.get("picture") != null) ? payload.get("picture").toString() : null);

            // Generate Token from User
            String token = jwtTokenService.generateToken(user.getUsername());
            log.info("Token: " + token);
            user.setToken(token);

            // Updating instance in db
            userService.saveAccess(user.getId(), instanceid, token, deviceid, manufacturer, model, device, kernel, version, sdk);

            log.info(user);

            // Return the token
            return ResponseEntity.ok(user);

        }catch (UsernameNotFoundException e){
            log.error(e, e);
            throw new Exception(e.getMessage());
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @DeleteMapping("destroy_token")
    public ResponseEntity<?> destroyToken(@RequestParam String token) throws Exception {
        log.info("call destroyToken: token:"+token);
        try {

            userService.destroyToken(token);

            return ResponseEntity.ok("Token destroyed!");

        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
