package pe.edu.tecsup.api.controllers.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.tecsup.api.models.*;
import pe.edu.tecsup.api.services.Studentervice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/api/student")
public class StudentController {

	private static final Logger log = Logger.getLogger(StudentController.class);

	@Autowired
	private Studentervice studentervice;

	@GetMapping("debts")
	public ResponseEntity<?> getDebts(@AuthenticationPrincipal User user) throws Exception{
		log.info("call getDebts: user:" + user);
		try {

			List<Debt> debts = studentervice.getDebts(user.getId());
			log.info("debts: " + debts);

			return ResponseEntity.ok(debts);
		}catch (Throwable e){
			log.error(e, e);
			throw e;
		}
	}

    @GetMapping("pays")
    public ResponseEntity<?> getPays(@AuthenticationPrincipal User user) throws Exception{
        log.info("call getPays: user:" + user);
        try {

            List<Pay> pays = studentervice.getPays(user.getId());
            log.info("pays: " + pays);

            return ResponseEntity.ok(pays);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("credits")
    public ResponseEntity<?> getCredits(@AuthenticationPrincipal User user) throws Exception{
        log.info("call getCredits: user:" + user);
        try {

            Credit credit = studentervice.getCredits(user.getId());
            log.info("credit: " + credit);

            if(credit == null)
                return new ResponseEntity<Object> (HttpStatus.NO_CONTENT);

            return ResponseEntity.ok(credit);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

	@GetMapping({"events", "events/{year}/{month}"})
	public ResponseEntity<?> getEvents(@AuthenticationPrincipal User user, @PathVariable(required = false) Integer year, @PathVariable(required = false) Integer month) throws Exception{
		log.info("call getEvents: user:" + user + " - year:" + year + " - month:" + month);
		try {

			List<Event> events = studentervice.getEvents(user.getId());

			if(year != null && month != null) {
                for (Iterator<Event> i = events.iterator(); i.hasNext(); ) {
                    Event event = i.next();
                    if (!(event.getYear() != null && event.getMonth() != null && event.getYear().equals(year) && event.getMonth().equals(month))) {
                        i.remove();
                    }
                }
            }

			log.info("events: " + events);

			return ResponseEntity.ok(events);
		}catch (Throwable e){
			log.error(e, e);
			throw e;
		}
	}

    @GetMapping("courses")
    public ResponseEntity<?> getCourses(@AuthenticationPrincipal User user) throws Exception{
        log.info("call getCourses: user:" + user);
        try {

            List<Course> courses = studentervice.getCourses(user.getId());
            log.info("courses: " + courses);

            return ResponseEntity.ok(courses);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("scores/{id}")
    public ResponseEntity<?> getScore(@AuthenticationPrincipal User user, @PathVariable Integer id) throws Exception{
        log.info("call getScore: user:" + user + " - id:" + id);
        try {

            Score scsore = studentervice.getScore(user.getId(), id);
            log.info("scsore: " + scsore);

            return ResponseEntity.ok(scsore);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("attendances/{id}")
    public ResponseEntity<?> getAttendance(@AuthenticationPrincipal User user, @PathVariable Integer id) throws Exception{
        log.info("call getAttendance: user:" + user + " - id:" + id);
        try {

            Attendance attendance = studentervice.getAttendance(user.getId(), id);
            log.info("attendance: " + attendance);

            return ResponseEntity.ok(attendance);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("history")
    public ResponseEntity<?> getHistory(@AuthenticationPrincipal User user) throws Exception{
        log.info("call getHistory: user:" + user);
        try {

            History history = studentervice.getHistory(user.getId());
            log.info("history: " + history);

            return ResponseEntity.ok(history);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
