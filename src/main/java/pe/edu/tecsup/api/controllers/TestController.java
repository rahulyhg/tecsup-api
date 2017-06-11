package pe.edu.tecsup.api.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.tecsup.api.tasks.ScheduledTask;

@Controller
public class TestController {

	private static final Logger log = Logger.getLogger(TestController.class);

	@Autowired
	private ScheduledTask scheduledTask;

	@GetMapping("/test")
	public ResponseEntity<?> test(Model model) throws Exception {
		log.info("calling test");

		scheduledTask.processingDealyDebts();

		return ResponseEntity.ok("OK");
	}

}
