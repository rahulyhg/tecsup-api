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

}
