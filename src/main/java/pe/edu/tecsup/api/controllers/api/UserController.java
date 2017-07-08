package pe.edu.tecsup.api.controllers.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("profile")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAuthenticatedProfile(@RequestHeader(value="Authorization") String token, @AuthenticationPrincipal User user) throws Exception{
        log.info("call getAuthenticatedProfile: "+token);
        try {

//            String username = jwtTokenService.parseToken(token, true);
//            log.info("Username: " + username);

//            User user = userService.loadUserByUsername(username);
            log.info("User: " + user);

            return ResponseEntity.ok(user);

        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("picture/{id}")
    public ResponseEntity<?> getPicture(@PathVariable Integer id) throws Exception{
        log.info("call getPicture: "+id);
        try {

            byte[] media = userService.loadThumbedUserPicture(id);
            log.info("User picture bytes: " + media.length);

//            return ResponseEntity.ok(media);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(media);

        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
