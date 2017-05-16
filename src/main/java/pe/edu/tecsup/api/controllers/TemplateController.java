package pe.edu.tecsup.api.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pe.edu.tecsup.api.services.TemplateService;

import java.util.List;

@Controller
@RequestMapping("/template")
public class TemplateController {

	private static final Logger log = Logger.getLogger(TemplateController.class);
	
	@Autowired
	private TemplateService templateService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) throws Exception {
		log.info("calling index");

		List<String> nombres = templateService.listar();
		model.addAttribute("nombres", nombres);

		return "template/index";
	}

}
