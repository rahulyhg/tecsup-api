package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.models.Cicle;
import pe.edu.tecsup.api.models.Major;
import pe.edu.tecsup.api.models.Seat;
import pe.edu.tecsup.api.models.Section;
import pe.edu.tecsup.api.repositories.CommonRepository;

import java.util.List;

@Service
public class CommonService {

	private static Logger log = Logger.getLogger(CommonService.class);
	
	@Autowired
	private CommonRepository commonRepository;

    public List<Seat> listSeats() throws Exception {
        return commonRepository.listSeats();
    }

    public List<Major> listMajors(String sede) throws Exception {
        return commonRepository.listMajors(sede);
    }

    public List<Cicle> listCicles(String sede, Integer codespecialidad) throws Exception {
        return commonRepository.listCicles(sede, codespecialidad);
    }

    public List<Section> listSections(String sede, Integer codespecialidad, Integer codcicle) throws Exception {
        return commonRepository.listSections(sede, codespecialidad, codcicle);
    }

}
