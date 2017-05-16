package pe.edu.tecsup.api.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.tecsup.api.models.CrmDeuda;
import pe.edu.tecsup.api.services.CrmService;

import java.util.List;

@Controller
@RequestMapping("/api/crm")
public class CrmController {

	private static final Logger log = Logger.getLogger(CrmController.class);

	@Autowired
	private CrmService crmService;

	@GetMapping("deudas/{numdocumento}/{tipdocumento}")
	public ResponseEntity<?> getdeudas(@PathVariable String numdocumento, @PathVariable String tipdocumento) throws Exception{
		log.info("call getdeudas: numdocumento:"+numdocumento);
		try {

			List<CrmDeuda> deudas = crmService.deudas(numdocumento);
			log.info("deudas: " + deudas);

			return ResponseEntity.ok(deudas);
		}catch (Throwable e){
			log.error(e, e);
			throw e;
		}
	}

}
