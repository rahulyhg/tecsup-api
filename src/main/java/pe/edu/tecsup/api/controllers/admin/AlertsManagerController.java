package pe.edu.tecsup.api.controllers.admin;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pe.edu.tecsup.api.models.Alert;
import pe.edu.tecsup.api.models.Student;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.AlertService;
import pe.edu.tecsup.api.utils.Mailer;
import pe.edu.tecsup.api.utils.Notifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ebenites on 2/07/2017.
 */
@Controller
@RequestMapping("/admin/alerts")
public class AlertsManagerController {

    private static final Logger log = Logger.getLogger(AlertsManagerController.class);

    @Autowired
    private AlertService alertService;

    @Autowired
    private Mailer mailer;

    @Autowired
    private Notifier notifier;

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

    @PostMapping("/save")
    public String save(Model model, RedirectAttributes redirectAttrs, @AuthenticationPrincipal User user, @RequestParam String content,
                       @RequestParam String sede, @RequestParam Integer formacion, @RequestParam Integer ciclo, @RequestParam Integer seccion) throws Exception {
        log.info("calling save " + user + " - content:" + content + " - sede:" + sede + " - formacion:" + formacion + " - ciclo:" + ciclo + " - seccion:" + seccion);
        try{

            List<Student> students = alertService.save(user.getId(), content, sede, formacion, ciclo, seccion);

            List<String> emails = new ArrayList<>();
            List<String> registrationIds = new ArrayList<>();
            for (Student student : students){
                if(student.getCorreo() != null)
                    emails.add(student.getCorreo());
                if(student.getInstanceid() != null)
                    registrationIds.add(student.getInstanceid());
            }

            // Test
            //emails.clear();
            //registrationIds.clear();
            emails.add("ebenites@tecsup.edu.pe");
            registrationIds.add("dpJQADvIjk8:APA91bEfrBD5RFMs1FW_6At9-kYqo_VyYbcuqpefXxHzSnq__ajx5RE-JylPF-cN7yENG3MIT0-AkqtZpV3rA4RfzW8Vhu4Pt-_GXmbHK5XRm-mDvFosxiIBMroWfAiVYSlAE42HkFBL");
            // End Test

            // Mailing
            mailer.sendMailByNotification(emails.toArray(new String[emails.size()]), content);

            // Notification
            notifier.notifyAlert(registrationIds, user.getName(), content);

            redirectAttrs.addFlashAttribute("message", "Notificación enviada correctamente");

            return "redirect:/admin/alerts/";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, RedirectAttributes redirectAttrs, @PathVariable Integer id) throws Exception {
        log.info("calling delete " + id);
        try{

            alertService.delete(id);

            redirectAttrs.addFlashAttribute("message", "Notificación eliminada correctamente");

            return "redirect:/admin/alerts/";
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

    @PostMapping("/edit/{id}")  // http://vitalets.github.io/x-editable/docs.html
    public ResponseEntity<?> edit(Model model, @PathVariable Integer id, @RequestParam Integer pk, @RequestParam String name, @RequestParam String value) throws Exception {
        log.info("calling edit id:" + id + " - field:" + name + " - ccntent:" + value);
        try{

            alertService.edit(id, value);

            Map<String, Boolean> message = new HashMap<>();
            message.put("success", true);

            return ResponseEntity.ok(message);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
