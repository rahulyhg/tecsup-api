package pe.edu.tecsup.api.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.api.models.CrmDeuda;
import pe.edu.tecsup.api.repositories.CrmRepository;

import java.util.List;

@Service
public class CrmService {

	private static Logger log = Logger.getLogger(CrmService.class);
	
	@Autowired
	private CrmRepository crmRepository;

	public List<CrmDeuda> deudas(String numdocumento) throws Exception {
        log.info("calling deudas: " + numdocumento);
        return crmRepository.deudas(numdocumento);
    }

}
