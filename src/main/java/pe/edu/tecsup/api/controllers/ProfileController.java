package pe.edu.tecsup.api.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class ProfileController {

	private static final Logger log = Logger.getLogger(ProfileController.class);

    @Autowired
    private HttpSession session;

    @Autowired
    private UserService userService;

	@GetMapping("/")
	public String index(Model model) throws Exception {
		log.info("calling index");

		return "profile/index";
	}

    @GetMapping("/picture")
    public ResponseEntity<?> getPicture(@AuthenticationPrincipal User user) throws Exception{
        log.info("call getPicture: " + user);
        try {

            byte[] media = userService.loadThumbedUserPicture(user.getId());
            log.info("User picture bytes: " + media.length);

//            return ResponseEntity.ok(media);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(media);

        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
