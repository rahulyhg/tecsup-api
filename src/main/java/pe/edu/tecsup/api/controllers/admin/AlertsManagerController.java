package pe.edu.tecsup.api.controllers.admin;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.tecsup.api.models.Alert;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.AlertService;

import java.util.List;

/**
 * Created by ebenites on 2/07/2017.
 */
@Controller
@RequestMapping("/admin/alerts")
public class AlertsManagerController {

    private static final Logger log = Logger.getLogger(AlertsManagerController.class);

    @Autowired
    private AlertService alertService;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal User user) throws Exception {
        log.info("calling index " + user);
        try{
            List<Alert> alerts = alertService.listBySender(user.getId());
            model.addAttribute("alerts", alerts);
            return "admin/alerts/index";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("/save")
    public String save(Model model, RedirectAttributes redirectAttrs, @AuthenticationPrincipal User user, @RequestParam String content) throws Exception {
        log.info("calling save " + user);
        try{

            alertService.save(user.getId(), content);

            redirectAttrs.addFlashAttribute("message", "Notificación enviada correctamente");
            return "redirect:/admin/alerts/";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
