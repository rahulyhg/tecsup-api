package pe.edu.tecsup.api.controllers.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.tecsup.api.models.Alert;
import pe.edu.tecsup.api.models.History;
import pe.edu.tecsup.api.models.User;
import pe.edu.tecsup.api.services.Studentervice;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/alerts")
public class AlertsController {

	private static final Logger log = Logger.getLogger(AlertsController.class);

	@Autowired
	private Studentervice studentervice;

    @GetMapping
    public ResponseEntity<?> getAlerts(@AuthenticationPrincipal User user) throws Exception{
        log.info("call getAlerts: user:" + user);
        try {

            List<Alert> alerts = new ArrayList<>();

            History history = studentervice.getHistory(user.getId());

            if(history.getIdcareer() == 28667 || history.getIdcareer() == 16) {
                Alert alert = new Alert();
                alert.setId(1L);
                alert.setFrom("Erick Benites");
                alert.setContent("Estimados alumnos, la recuperación de nuestra clase será hoy en el aula 1509.");
                alert.setDate("16/05/2017");
                alert.setViewed(true);
                alerts.add(alert);
            }

            log.info("alerts: " + alerts);

            return ResponseEntity.ok(alerts);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
