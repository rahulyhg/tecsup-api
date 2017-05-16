package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.models.*;
import pe.edu.tecsup.api.repositories.StudentRepository;

import java.util.List;

@Service
public class Studentervice {

	private static Logger log = Logger.getLogger(Studentervice.class);
	
	@Autowired
	private StudentRepository studentRepository;

	public List<Debt> getDebts(Integer id) throws Exception {
		log.info("calling getDebts: " + id);
		return studentRepository.getDebts(id);
	}

	public List<Event> getEvents(Integer id) throws Exception {
		log.info("calling getEvents: " + id);
		return studentRepository.getEvents(id);
	}

	public List<Course> getCourses(Integer id) throws Exception {
		log.info("calling getCourses: " + id);
		return studentRepository.getCourses(id);
	}

    public Score getScore(Integer id, Integer idcourse) throws Exception {
		log.info("calling getScore: " + id + " - " + idcourse);
		return studentRepository.getScoreMaster(id, idcourse);
	}

    public Attendance getAttendance(Integer id, Integer idcourse) throws Exception {
        log.info("calling getAttendance: " + id + " - " + idcourse);
        return studentRepository.getAttendanceMaster(id, idcourse);
    }

}
