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

	@GetMapping({"/seats/{seat}/majors", "/seats//majors"})
	public ResponseEntity<?> majors(@PathVariable(required = false) String seat) throws Exception {
		log.info("calling majors: " + seat);
		List<Major> majors = commonService.listMajors(seat);
		return ResponseEntity.ok(majors);
	}

	@GetMapping({"/seats/{seat}/majors/{majorid}/cicles", "/seats/{seat}/majors//cicles", "/seats//majors/{majorid}/cicles", "/seats//majors//cicles"})
	public ResponseEntity<?> cicles(@PathVariable(required = false) String seat, @PathVariable(required = false) Integer majorid) throws Exception {
		log.info("calling cicles: " + seat + " - " + majorid);
		List<Cicle> cicles = commonService.listCicles(seat, majorid);
		return ResponseEntity.ok(cicles);
	}

	@GetMapping({"/seats/{seat}/majors/{majorid}/cicles/{cicleid}/sections", "/seats/{seat}/majors/{majorid}/cicles//sections", "/seats/{seat}/majors//cicles/{cicleid}/sections", "/seats//majors/{majorid}/cicles/{cicleid}/sections", "/seats/{seat}/majors//cicles//sections", "/seats//majors//cicles/{cicleid}/sections", "/seats//majors/{majorid}/cicles//sections", "/seats//majors//cicles//sections"})
	public ResponseEntity<?> sections(@PathVariable(required = false) String seat, @PathVariable(required = false) Integer majorid, @PathVariable(required = false) Integer cicleid) throws Exception {
		log.info("calling sections: " + seat + " - " + majorid + " - " + cicleid);
		List<Section> sections = commonService.listSections(seat, majorid, cicleid);
		return ResponseEntity.ok(sections);
	}

}
