package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.repositories.TemplateRepository;

import java.util.List;

@Service
public class TemplateService {

	private static Logger log = Logger.getLogger(TemplateService.class);
	
	@Autowired
	private TemplateRepository templateDao;
	
	public List<String> listar() {
		log.info("calling listar: ");
		
		return templateDao.listar();
	}

}
