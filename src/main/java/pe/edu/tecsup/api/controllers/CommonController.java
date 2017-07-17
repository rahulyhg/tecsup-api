package pe.edu.tecsup.api.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.tecsup.api.models.Cicle;
import pe.edu.tecsup.api.models.Major;
import pe.edu.tecsup.api.models.Seat;
import pe.edu.tecsup.api.models.Section;
import pe.edu.tecsup.api.services.CommonService;
import pe.edu.tecsup.api.tasks.ScheduledTask;

import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonController {

	private static final Logger log = Logger.getLogger(CommonController.class);

	@Autowired
	private CommonService commonService;

	@GetMapping("/seats")
	public ResponseEntity<?> seats() throws Exception {
		log.info("calling seats");
		List<Seat> seats = commonService.listSeats();
		return ResponseEntity.ok(seats);
	}

	@GetMapping("/seats/{seat}/majors")
	public ResponseEntity<?> majors(@PathVariable String seat) throws Exception {
		log.info("calling majors: " + seat);
		List<Major> majors = commonService.listMajors(seat);
		return ResponseEntity.ok(majors);
	}

	@GetMapping("/seats/{seat}/majors/{majorid}/cicles")
	public ResponseEntity<?> cicles(@PathVariable String seat, @PathVariable Integer majorid) throws Exception {
		log.info("calling cicles: " + seat + " - " + majorid);
		List<Cicle> cicles = commonService.listCicles(seat, majorid);
		return ResponseEntity.ok(cicles);
	}

	@GetMapping("/seats/{seat}/majors/{majorid}/cicles/{cicleid}/sections")
	public ResponseEntity<?> sections(@PathVariable String seat, @PathVariable Integer majorid, @PathVariable Integer cicleid) throws Exception {
		log.info("calling sections: " + seat + " - " + majorid + " - " + cicleid);
		List<Section> sections = commonService.listSections(seat, majorid, cicleid);
		return ResponseEntity.ok(sections);
	}

}
