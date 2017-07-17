package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.models.Alert;
import pe.edu.tecsup.api.models.Student;
import pe.edu.tecsup.api.repositories.AlertRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AlertService {

	private static Logger log = Logger.getLogger(AlertService.class);
	
	@Autowired
	private AlertRepository alertRepository;

    public List<Alert> listByStudent(Integer id) throws Exception {
        return alertRepository.listByStudent(id);
    }

    public List<Alert> listBySender(Integer id) throws Exception {
        return alertRepository.listBySender(id);
    }

    @Transactional
    public List<Student> save(Integer senderid, String content, String sede, Integer formacion, Integer ciclo, Integer seccion)throws Exception {
        return alertRepository.save(senderid, content, sede, formacion, ciclo, seccion);
    }

    public void delete(Integer id) throws Exception {
        alertRepository.delete(id);
    }

    public void edit(Integer id, String content) throws Exception {
        alertRepository.edit(id, content);
    }

}
