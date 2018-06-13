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
import pe.edu.tecsup.api.services.AlertService;
import pe.edu.tecsup.api.services.StudentService;
import pe.edu.tecsup.api.services.TeacherService;
import pe.edu.tecsup.api.utils.Constant;
import pe.edu.tecsup.api.utils.Mailer;
import pe.edu.tecsup.api.utils.Notifier;
import retrofit2.Response;

import java.util.*;

@Controller
@RequestMapping("/api/teacher")
public class TeacherController {

	private static final Logger log = Logger.getLogger(TeacherController.class);

	@Autowired
	private TeacherService teacherService;

    @Autowired
    private StudentService studentervice;

    @Autowired
    private AlertService alertService;

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

//            if ("erick.benites@tecsup.edu.pe".equals(user.getEmail()))
//                supportPortal.setPhoneNumber(teacherService.getPhoneNumber("cX-_m77cWbI:APA91bGtGlPlcJ1kcabVsgDdneLC41PjNfdTZYLu5UmT9Yk3ypf0r4PkWqyMKurog06SfmcxQI6YxfZYusNxLoF7XlZJt8039AU3SGNIA3QvWkaw4mFPbcwF7EqHqFpBAnUuGm37BQWZ"));

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

            if(!user.hasRole(Constant.ROLE_SEVA_DOCENTE))
                throw new Exception("Acceso no autorizado a su rol");

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

            if(!user.hasRole(Constant.ROLE_SEVA_DOCENTE))
                throw new Exception("Acceso no autorizado a su rol");

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
//            emails.clear();
//            registrationIds.clear();
//            emails.add("ebenites@tecsup.edu.pe");
//            registrationIds.add("ccF3KPsH3VM:APA91bHr-HP1o_mBAR4l52NoqlclGwfxfono-Nvlb3kghdTPAY-0Rrev5o6dvUheELJtar4yBUq5XfJcucUkYQdl1bHhoHB1TO4UGkXpAUCVS7_Ja_ChKsMw_rMr_3FwfJjkLfiyyWzZ");
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

    @GetMapping({"incident", "incident/{status}"})
    public ResponseEntity<?> getIncidentsByStatus(@AuthenticationPrincipal User user, @PathVariable(required = false) String status) throws Exception {
        log.info("call getIncidentsByStatus: user:" + user + " - status:" + status);
        try {

            List<Incident> incidents = new ArrayList<>();

            if(status == null)
                incidents = teacherService.getAllIncidents();
            else{
                if(Constant.INCIDENT_STATUS_PENDIENT.equals(status)){
                    incidents = teacherService.getIncidents(user.getSede(), status);
                }else if(Constant.INCIDENT_STATUS_ATENTION.equals(status) || Constant.INCIDENT_STATUS_CLOSED.equals(status)){
                    incidents = teacherService.getIncidentsByTechnical(user.getId(), status);
                }
            }

            log.info(incidents);

            return ResponseEntity.ok(incidents);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @PatchMapping("incident/{id}")
    public ResponseEntity<?> patchIncidentsByStatus(@AuthenticationPrincipal User user, @PathVariable Integer id, @RequestParam String status) throws Exception {
        log.info("call patchIncidentsByStatus: user:" + user + " - status:" + status);
        try {

            if(!Constant.INCIDENT_STATUS_ATENTION.equals(status) && !Constant.INCIDENT_STATUS_CLOSED.equals(status))
                throw new Exception("Estado " + status + " no permitido");

            Incident incident = teacherService.getIncident(id);

            if(Constant.INCIDENT_STATUS_ATENTION.equals(status) && !Constant.INCIDENT_STATUS_PENDIENT.equals(incident.getStatus()))
                throw new Exception("El ticket ya fue tomado por " + incident.getTechnical());

            teacherService.updateIncident(id, user.getId(), status);
            incident = teacherService.getIncident(id);

            // Notification
            Customer customer = teacherService.getCustomer(incident.getCustomerid());
            log.info(customer);

            // Test
//            customer.getInstancesid().clear();
//            customer.setEmail("ebenites@tecsup.edu.pe");
//            customer.getInstancesid().add("ccF3KPsH3VM:APA91bHr-HP1o_mBAR4l52NoqlclGwfxfono-Nvlb3kghdTPAY-0Rrev5o6dvUheELJtar4yBUq5XfJcucUkYQdl1bHhoHB1TO4UGkXpAUCVS7_Ja_ChKsMw_rMr_3FwfJjkLfiyyWzZ");
            // End Test

            // Mailing
            mailer.sendMailAttention(customer.getEmail(), incident);

            // Notification
            notifier.notifyIncidentAttention(customer.getInstancesid(), incident);

            log.info(incident);

            return ResponseEntity.ok(incident);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("courses/{id}/detail")
    public ResponseEntity<?> getCourseDetail(@AuthenticationPrincipal User user, @PathVariable Integer id) throws Exception{
        log.info("call getCourseDetail: user:" + user + " - courseid:" + id);
        try {

            CourseDetail courseDetail = new CourseDetail();

            List<Student> students = teacherService.getStudentsbyCourse(id);
            log.info("students: " + students);
            courseDetail.setStudents(students);

            for (Student student : students) {
                log.info("information by: " + student);

                Score scsore = studentervice.getScore(student.getId(), id);
                log.info("scsore: " + scsore);
                student.setScore(scsore);

                Attendance attendance = studentervice.getAttendance(student.getId(), id);
                log.info("attendance: " + attendance);
                student.setAttendance(attendance);

            }

            Course course = teacherService.getSections(id);
            log.info("course: " + course);
            courseDetail.setCourse(course);

            Set<String> assignmentsUnsorted = new HashSet<>();
            for (Student student : students) {
                Score score = student.getScore();

                if(score.getTheoId() != null)
                    assignmentsUnsorted.add("1" + score.getTheoTitle());

                List<Score.Item> theos = score.getTheos();
                for (Score.Item item : theos) {
                    if("1".equals(item.getExecuted())) {
                        item.setTitle(item.getTitle() + " de Aula");
                        assignmentsUnsorted.add("1" + item.getTitle());
                    }
                }

                if(score.getLabId() != null)
                    assignmentsUnsorted.add("2" + score.getLabTitle());

                List<Score.Item> labs = score.getLabs();
                for (Score.Item item : labs) {
                    if("1".equals(item.getExecuted())) {
                        item.setTitle(item.getTitle() + " de Laboratorio");
                        assignmentsUnsorted.add("2" + item.getTitle());
                    }
                }

                if(score.getWorkId() != null)
                    assignmentsUnsorted.add("3" + score.getWorkTitle());

                List<Score.Item> works = score.getWorks();
                for (Score.Item item : works) {
                    if("1".equals(item.getExecuted())) {
                        item.setTitle(item.getTitle() + " de Taller");
                        assignmentsUnsorted.add("3" + item.getTitle());
                    }
                }

                if(score.getPartialId() != null)
                    assignmentsUnsorted.add("4" + score.getPartialTitle());

                if(score.getFinalId() != null)
                    assignmentsUnsorted.add("5" + score.getFinalTitle());

            }

            assignmentsUnsorted.add("6Promedio Móvil");

            List<String> assignmentsSorted = new ArrayList<>(assignmentsUnsorted);
            Collections.sort(assignmentsSorted);

            List<String> assignments = new ArrayList<>();
            for (String title : assignmentsSorted) {
                assignments.add(title.substring(1, title.length()));
            }

            log.info("assignmentsUnsorted: " + assignments);
            courseDetail.setAssignments(assignments);

            log.info("courseDetail: " + courseDetail);

            return ResponseEntity.ok(courseDetail);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("courses/{courseid}/students/{studentid}/scores")
    public ResponseEntity<?> getScore(@AuthenticationPrincipal User user, @PathVariable Integer courseid, @PathVariable Integer studentid) throws Exception{
        log.info("call getScore: user:" + user + " - courseid:" + courseid + " - studentid:" + studentid);
        try {

            if(!user.hasRole(Constant.ROLE_SEVA_DOCENTE))
                throw new Exception("Acceso no autorizado a su rol");

            Score scsore = studentervice.getScore(studentid, courseid);
            log.info("scsore: " + scsore);

            return ResponseEntity.ok(scsore);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("courses/{courseid}/students/{studentid}/attendances")
    public ResponseEntity<?> getAttendance(@AuthenticationPrincipal User user, @PathVariable Integer courseid, @PathVariable Integer studentid) throws Exception{
        log.info("call getAttendance: user:" + user + " - courseid:" + courseid + " - studentid:" + studentid);
        try {

            if(!user.hasRole(Constant.ROLE_SEVA_DOCENTE))
                throw new Exception("Acceso no autorizado a su rol");

            Attendance attendance = studentervice.getAttendance(studentid, courseid);
            log.info("attendance: " + attendance);

            return ResponseEntity.ok(attendance);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("students/{id}/history")
    public ResponseEntity<?> getHistory(@AuthenticationPrincipal User user, @PathVariable Integer id) throws Exception{
        log.info("call getHistory: user:" + user + " - id:" + id);
        try {

            if(!user.hasRole(Constant.ROLE_SEVA_DOCENTE))
                throw new Exception("Acceso no autorizado a su rol");

            History history = studentervice.getHistory(id);
            log.info("history: " + history);

            return ResponseEntity.ok(history);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("sections")
    public ResponseEntity<?> getSections(@AuthenticationPrincipal User user) throws Exception{
        log.info("call getSections: user:" + user);
        try {

            List<Section> sections = teacherService.getSectionsByTeacher(user.getId());
            log.info("sections: " + sections);

            return ResponseEntity.ok(sections);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @PostMapping("alerts")
    public ResponseEntity<?> saveAlert(@AuthenticationPrincipal User user, @RequestParam String content, @RequestParam(value = "secciones[]") Integer[] secciones) throws Exception {
        log.info("calling saveAlert " + user + " - content:" + content + " - secciones:" + secciones);
        try{

            List<Student> students = teacherService.saveAlert(user.getId(), content, secciones);

            List<String> emails = new ArrayList<>();
            List<String> registrationIds = new ArrayList<>();
            for (Student student : students){
                if(student.getCorreo() != null)
                    emails.add(student.getCorreo());
                if(student.getInstanceid() != null)
                    registrationIds.add(student.getInstanceid());
            }

            // Test
            //emails.clear();
            //registrationIds.clear();
            //emails.add("ebenites@tecsup.edu.pe");
            //registrationIds.add("fehgvgOZlok:APA91bEC3BWilHwnh9dOu-Lr1ahoVx6Q2CWwCIK9zz2EOg6_mMJjZQg3kLWMM6ps0VWYUi3PMxyJOnPtbzhHxn3QN0MViXjdP5kgNGPqQV_yVE4nem0KK_VpsPpoE4Tx_OIGrT3AkRnK");
            // End Test

            // Mailing
            mailer.sendMailByNotification(emails.toArray(new String[emails.size()]), content);

            // Notification
            notifier.notifyAlert(registrationIds, user.getName(), content);

            return ResponseEntity.ok(APIMessage.create("Alerta enviada satisfatoriamente"));
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("alerts")
    public ResponseEntity<?> getAlerts(@AuthenticationPrincipal User user) throws Exception {
        log.info("calling getAlerts " + user);
        try{
            List<Alert> alerts = alertService.listBySender(user.getId());

            return ResponseEntity.ok(alerts);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
