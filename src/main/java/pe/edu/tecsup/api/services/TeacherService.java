package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.models.*;
import pe.edu.tecsup.api.repositories.TeacherRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TeacherService {

	private static Logger log = Logger.getLogger(TeacherService.class);
	
	@Autowired
	private TeacherRepository teacherRepository;

	public List<Event> getEvents(Integer id) throws Exception {
		log.info("calling getEvents: " + id);
		return teacherRepository.getEvents(id);
	}

	public List<Course> getCourses(Integer id) throws Exception {
		log.info("calling getCourses: " + id);
		return teacherRepository.getCourses(id);
	}

	public Course getSections(Integer id) throws Exception {
		log.info("calling getSecciones: " + id);
		return teacherRepository.getSections(id);
	}

    public CourseDashboard getMetrics(Integer codcursoejec, Integer codseccion) throws Exception {
        log.info("calling getMetrics: " + codcursoejec + " - codseccion:" + codseccion);
        return teacherRepository.getMetrics(codcursoejec, codseccion);
    }

    public List<Seat> getSeatsWithLocations() throws Exception {
        log.info("calling getSeatsWithLocations" );
        return teacherRepository.getSeatsWithLocations();
    }

    public PhoneNumber getPhoneNumber(String instanceid) throws Exception {
        log.info("calling getPhoneNumber: " + instanceid);
        return teacherRepository.getPhoneNumber(instanceid);
    }

    public PhoneNumber getPhoneNumber(Long id) throws Exception {
        log.info("calling getPhoneNumber: " + id);
        return teacherRepository.getPhoneNumber(id);
    }

    @Transactional
    public void insertPhoneNumber(String instanceid, String countrycode, String phonenumber) throws Exception {
        log.info("calling insertPhoneNumber: instanceid:" + instanceid + " - countrycode:" + " - phonenumber:" + phonenumber );
        teacherRepository.insertPhoneNumber(instanceid, countrycode, phonenumber);
    }

    public void activatePhoneNumber(Long id) throws Exception {
        log.info("calling activatePhoneNumber: " + id);
        teacherRepository.activatePhoneNumber(id);
    }

    public PhoneNumber getActivedPhoneNumber(String instanceid) throws Exception {
        log.info("calling getActivedPhoneNumber: " + instanceid);
        return teacherRepository.getActivedPhoneNumber(instanceid);
    }

    public Integer saveIncident(Integer customerid, String phone, String sede, String location) throws Exception {
        log.info("saveIncident: customerid:" + customerid + ", phone:" + phone + ", sede:" + sede + ", location:" + location);
        return teacherRepository.saveIncident(customerid, phone, sede, location);
    }

    public Incident getIncident(Integer id) throws Exception {
        log.info("getIncident: " + id);
        return teacherRepository.getIncident(id);
    }

    public List<Technical> getTechnicalForNotification(String sede) throws Exception {
        log.info("getTechnicalForNotification: " + sede);
        return teacherRepository.getTechnicalForNotification(sede);
    }

    public List<Incident> getIncidents(String sede, String status) throws Exception {
        log.info("getIncidents: " + sede);
        return teacherRepository.getIncidents(sede, status);
    }

    public List<Incident> getIncidentsByTechnical(Integer userid, String status) throws Exception {
        log.info("getIncidentsByTechnical: " + userid + " - status:" + status);
        return teacherRepository.getIncidentsByTechnical(userid, status);
    }

    public List<Incident> getAllIncidents() throws Exception {
        log.info("getAllIncidents: ");
        return teacherRepository.getAllIncidents();
    }

    public void updateIncident(Integer id, Integer technicalid, String status) throws Exception {
        log.info("updateIncident: id:"+id+", technicalid:" + technicalid + ", status:" + status);
        teacherRepository.updateIncident(id, technicalid, status);
    }

    public Customer getCustomer(Integer id) throws Exception {
        log.info("getCustomer: " + id);
        return teacherRepository.getCustomer(id);
    }

    public List<Student> getStudentsbyCourse(Integer codcursoejec) throws Exception {
        log.info("getStudentsbyCourse: " + codcursoejec);
        return teacherRepository.getStudentsbyCourse(codcursoejec);
    }

    public List<Section> getSectionsByTeacher(Integer id) throws Exception {
        log.info("getSectionsByTeacher(" + id + ")");
        return teacherRepository.getSectionsByTeacher(id);
    }

    public List<Student> saveAlert(Integer senderid, String content, Integer[] codsecciones) throws Exception {
        log.info("saveAlert(" + senderid + ":" + content + ", " + codsecciones + ")");
        return teacherRepository.saveAlert(senderid, content, codsecciones);
    }

}
