package pe.edu.tecsup.api.controllers.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.api.models.*;
import pe.edu.tecsup.api.remotes.twilio.TwilioService;
import pe.edu.tecsup.api.remotes.twilio.TwilioServiceGenerator;
import pe.edu.tecsup.api.remotes.twilio.models.ResponseChecking;
import pe.edu.tecsup.api.remotes.twilio.models.ResponseVerification;
import pe.edu.tecsup.api.services.TeacherService;
import pe.edu.tecsup.api.utils.Constant;
import pe.edu.tecsup.api.utils.Mailer;
import pe.edu.tecsup.api.utils.Notifier;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/api/teacher")
public class TeacherController {

	private static final Logger log = Logger.getLogger(TeacherController.class);

	@Autowired
	private TeacherService teacherService;

    @Autowired
    private Mailer mailer;

    @Autowired
    private Notifier notifier;

    @Value("${twilio.key}")
    public String TWILIO_API_KEY;

	@GetMapping({"events", "events/{year}/{month}"})
	public ResponseEntity<?> getEvents(@RequestHeader(value="Authorization") String token, @AuthenticationPrincipal User user, @PathVariable(required = false) Integer year, @PathVariable(required = false) Integer month) throws Exception{
		log.info("call getEvents: user:" + user + " - year:" + year + " - month:" + month);
		try {

			List<Event> events = teacherService.getEvents(user.getId());

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

            List<Course> courses = teacherService.getCourses(user.getId());
            log.info("courses: " + courses);

            return ResponseEntity.ok(courses);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("courses/{id}/sections")
    public ResponseEntity<?> getSections(@AuthenticationPrincipal User user, @PathVariable Integer id) throws Exception{
        log.info("call getSections: user:" + user + " - id:" + id);
        try {

            Course course = teacherService.getSections(id);
            log.info("course: " + course);

            return ResponseEntity.ok(course);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping({"courses/{id}/metrics", "courses/{id}/sections/{sectionid}/metrics"})
    public ResponseEntity<?> getMetrics(@AuthenticationPrincipal User user, @PathVariable Integer id, @PathVariable(required = false) Integer sectionid) throws Exception{
        log.info("call getMetrics: user:" + user + " - courseid:" + id + " - sectionid:" + sectionid);
        try {

            CourseDashboard courseDashboard = teacherService.getMetrics(id, sectionid);
            log.info("courseDashboard: " + courseDashboard);

            return ResponseEntity.ok(courseDashboard);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("phonenumber")
    public ResponseEntity<?> getPhoneNumber(@AuthenticationPrincipal User user, @RequestParam("instanceid") String instanceid) throws Exception{
        log.info("call getPhoneNumber: user:" + user + " - instanceid:" + instanceid);
        try {

            SupportPortal supportPortal = new SupportPortal();
            supportPortal.setSeats(teacherService.getSeatsWithLocations());

            PhoneNumber phoneNumber = teacherService.getPhoneNumber(instanceid);
//            if(phoneNumber.getActivated())
                supportPortal.setPhoneNumber(phoneNumber);

            log.info("supportPortal: " + supportPortal);

            return ResponseEntity.ok(supportPortal);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @PostMapping("phonenumber")
    public ResponseEntity<?> insertPhoneNumber(@AuthenticationPrincipal User user, @RequestParam String instanceid, @RequestParam String countrycode, @RequestParam String phonenumber) throws Exception {
        log.info("call insertPhoneNumber: user:"+user+" - instanceid:"+instanceid+" - countrycode:"+countrycode+" - phonenumber:"+phonenumber);
        try {

            if(user.getRole() != Constant.ROLE_SEVA_DOCENTE)
                throw new Exception("Acceso restringido");

            PhoneNumber phoneNumber = teacherService.getPhoneNumber(instanceid);

            if(phoneNumber!= null && phoneNumber.getPhoneNumber().equals(phonenumber) && phoneNumber.getActivated()){
                throw new Exception("Ya tiene activo este mismo número");
            }

            TwilioService service = TwilioServiceGenerator.getClient().create(TwilioService.class);
            Response<ResponseVerification> response = service.startPhoneVerification(TWILIO_API_KEY, "sms", countrycode, phonenumber).execute();
            log.info("HTTP response: " + response);

            int statusCode = response.code();
            log.info("HTTP status code: " + statusCode);

            if(!response.isSuccessful()) {
                throw new Exception("Error con el servicio de mensajería");
            }

            ResponseVerification responseVerification = response.body();
            log.info("responseVerification: " + responseVerification);

            if(!responseVerification.getSuccess()) {
                throw new Exception("Error con el servicio de mensajería");
            }

            teacherService.insertPhoneNumber(instanceid, countrycode, phonenumber);

            phoneNumber = teacherService.getPhoneNumber(instanceid);
            log.info(phoneNumber);

            return ResponseEntity.ok(phoneNumber);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @PostMapping("phonenumber/{id}")
    public ResponseEntity<?> activatePhoneNumber(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestParam String code) throws Exception {
        log.info("call activatePhoneNumber: user:"+user+" - id:"+id + " - code:" + code);
        try {

            if(user.getRole() != Constant.ROLE_SEVA_DOCENTE)
                throw new Exception("Acceso restringido");

            PhoneNumber phoneNumber = teacherService.getPhoneNumber(id);
            log.info(phoneNumber);

            TwilioService service = TwilioServiceGenerator.getClient().create(TwilioService.class);
            Response<ResponseChecking> response = service.checkPhoneVerification(TWILIO_API_KEY, phoneNumber.getCountryCode(), phoneNumber.getPhoneNumber(), code).execute();
            log.info("HTTP response: " + response);

            int statusCode = response.code();
            log.info("HTTP status code: " + statusCode);

            if(!response.isSuccessful()) {
                throw new Exception("Error con el servicio de mensajería");
            }

            ResponseChecking responseChecking = response.body();
            log.info("responseChecking: " + responseChecking);

            if(!responseChecking.getSuccess()) {
                throw new Exception("Error con el servicio de mensajería");
            }

            teacherService.activatePhoneNumber(id);

            phoneNumber = teacherService.getPhoneNumber(id);
            log.info(phoneNumber);

            return ResponseEntity.ok(phoneNumber);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @PostMapping("incident")
    public ResponseEntity<?> saveIncident(@AuthenticationPrincipal User user, @RequestParam String instanceid, @RequestParam String sede, @RequestParam String location) throws Exception {
        log.info("call saveIncident: user:"+user+" - instanceid:" + instanceid + " - sede:" + sede + " - location:" + location);
        try {

            if(user == null)
                throw new IllegalAccessException("Acceso no Autorizado");

            PhoneNumber phoneNumber = teacherService.getActivedPhoneNumber(instanceid);
            log.info(phoneNumber);

            Integer id = teacherService.saveIncident(user.getId(), (phoneNumber!=null?phoneNumber.getFullPhoneNumber():null), sede, location);
            log.info(id);

            Incident incident = teacherService.getIncident(id);
            log.info(incident);

            List<Technical> technicals = teacherService.getTechnicalForNotification(sede);
            log.info(technicals);

            List<String> emails = new ArrayList<>();
            List<String> registrationIds = new ArrayList<>();
            for (Technical technical : technicals) {
                if(technical.getInstanceid() != null)
                    registrationIds.add(technical.getInstanceid());
                if(technical.getEmail() != null)
                    emails.add(technical.getEmail());
            }

            // Test
            emails.clear();
            registrationIds.clear();
            emails.add("ebenites@tecsup.edu.pe");
            registrationIds.add("ccF3KPsH3VM:APA91bHr-HP1o_mBAR4l52NoqlclGwfxfono-Nvlb3kghdTPAY-0Rrev5o6dvUheELJtar4yBUq5XfJcucUkYQdl1bHhoHB1TO4UGkXpAUCVS7_Ja_ChKsMw_rMr_3FwfJjkLfiyyWzZ");
            // End Test

            // Mailing
            mailer.sendMailByIncident(emails.toArray(new String[emails.size()]), user, incident);

            // Notification
            notifier.notifyIncident(registrationIds, user.getName(), location);

            return ResponseEntity.ok(incident);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("incident/{status}")
    public ResponseEntity<?> getIncidentsByStatus(@AuthenticationPrincipal User user, @PathVariable String status) throws Exception {
        log.info("call getIncidentsByStatus: user:" + user + " - status:" + status);
        try {

            List<Incident> incidents = teacherService.getIncidents(user.getSede(), status);
            log.info(incidents);

            return ResponseEntity.ok(incidents);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
