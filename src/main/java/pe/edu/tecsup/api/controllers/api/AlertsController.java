package pe.edu.tecsup.api.controllers.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.tecsup.api.models.Alert;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.AlertService;

import java.util.List;

@Controller
@RequestMapping("/api/alerts")
public class AlertsController {

	private static final Logger log = Logger.getLogger(AlertsController.class);

	@Autowired
	private AlertService alertService;

    @GetMapping
    public ResponseEntity<?> getAlerts(@AuthenticationPrincipal User user) throws Exception{
        log.info("call getAlerts: user:" + user);
        try {

            List<Alert> alerts = alertService.listByStudent(user.getId());
            log.info("alerts: " + alerts);

            return ResponseEntity.ok(alerts);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
