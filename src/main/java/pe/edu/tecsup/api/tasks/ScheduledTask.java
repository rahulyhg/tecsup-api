package pe.edu.tecsup.api.tasks;

import com.liferay.mobile.fcm.Message;
import com.liferay.mobile.fcm.Notification;
import com.liferay.mobile.fcm.Sender;
import com.liferay.mobile.fcm.Status;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.edu.tecsup.api.remotes.apixu.OpenWeatherService;
import pe.edu.tecsup.api.remotes.apixu.WeatherApiClient;
import pe.edu.tecsup.api.remotes.apixu.models.ForecastWeather;
import pe.edu.tecsup.api.services.Studentervice;
import pe.edu.tecsup.api.utils.Constant;
import pe.edu.tecsup.api.utils.Mailer;

import javax.servlet.ServletContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ebenites on 16/05/2017.
 */
@Component
public class ScheduledTask {

    private static final Logger log = Logger.getLogger(ScheduledTask.class);

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private Studentervice studentervice;

    @Autowired
    private Mailer mailer;

    @Scheduled(cron="0 * * * * *")
    public void keepalive(){
        log.info(new Date() + " - keep alive ScheduledTask ...");
    }

    @Value("${fcm.serverkey}")
    private String FCMSERVERKEY;

    @Scheduled(cron="0 0 8 * * *")
    public void processingDealyDebts() {
        log.info("processingDealyDebts ...");
        try {

            Sender sender = new Sender(FCMSERVERKEY);

            // Instances list
            List<String> registrationIds = studentervice.getDebtorInstancesDealy();

            // Build Notification Payload
            Notification notification = new Notification.Builder()
                    .title("¡Hoy vence tu deuda!")
                    .body("Evita las moras, paga a tiempo")
                    .clickAction(".activities.MainActivity")    // https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase#42279260
                    .icon("ic_payment")
//                    .color("")
                    .sound("default")
                    .build();

            Map<String, Object> payload = new HashMap<>();
            payload.put(Constant.FIREBASE_PAYLOAD_GO, Constant.FIREBASE_PAYLOAD_GO_DEBT);

            // Compose a Message:
            Message message = new Message.Builder()
                    .to(registrationIds)
                    .data(payload)
                    .notification(notification)
                    .build();

            // Send a Message:
            Status status = sender.send(message);

            // Show status:
            log.info("Response: " + status);

        }catch (Exception e){
            log.error(e, e);
            mailer.sendMailToAdministrator("ERROR en ScheduledTask:processingDealyDebts", e.toString());
        }
    }


    @Value("${apixu.key}")
    public String WEATHER_API_KEY;


    @Scheduled(cron="0 0 * * * *")
    public void processingWeatherUpdating() {
        log.info("processingWeatherUpdating ...");
        try {

            OpenWeatherService service = WeatherApiClient.getClient().create(OpenWeatherService.class);

            // https://api.apixu.com/v1/current.json?lang=es&q=Lima&key=*******************************
            ForecastWeather limaWeather = service.getWeather(WEATHER_API_KEY, "Lima").execute().body();
            servletContext.setAttribute("weather-L", limaWeather);
            log.info(limaWeather);

            // https://api.apixu.com/v1/current.json?lang=es&q=Arequipa&key=*******************************
            ForecastWeather arequipaWeather = service.getWeather(WEATHER_API_KEY, "Arequipa").execute().body();
            servletContext.setAttribute("weather-A", arequipaWeather);
            log.info(arequipaWeather);

            // https://api.apixu.com/v1/current.json?lang=es&q=Trujillo&key=*******************************
            ForecastWeather trujilloWeather = service.getWeather(WEATHER_API_KEY, "Trujillo").execute().body();
            servletContext.setAttribute("weather-T", trujilloWeather);
            log.info(trujilloWeather);

        }catch (Exception e){
            log.error(e, e);
            mailer.sendMailToAdministrator("ERROR en ScheduledTask:processingWeatherUpdating", e.toString());
        }
    }
}
