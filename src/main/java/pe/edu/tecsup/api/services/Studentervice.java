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

    public List<Pay> getPays(Integer id) throws Exception {
		log.info("calling getPays: " + id);
		return studentRepository.getPays(id);
	}

    public Credit getCredits(Integer id) throws Exception {
		log.info("calling getCredits: " + id);
		return studentRepository.getCredits(id);
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

    public History getHistory(Integer id) throws Exception {
        log.info("calling getHistory: " + id);
        return studentRepository.getHistory(id);
    }

    public List<String> getDebtorInstancesDealy() throws Exception {
        log.info("calling getDebtorInstancesDealy: ");
        return studentRepository.getDebtorInstancesDealy();
    }

	@Deprecated
	public String getDeviceByToken(String tokenid) {
		return studentRepository.getDeviceByToken(tokenid);
	}

}
