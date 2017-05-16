package pe.edu.tecsup.api.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

	private static final Logger log = Logger.getLogger(HomeController.class);

	@GetMapping("/")
	public String home(Model model) throws Exception {
		log.info("calling home");

		return "home/index";
	}

}
