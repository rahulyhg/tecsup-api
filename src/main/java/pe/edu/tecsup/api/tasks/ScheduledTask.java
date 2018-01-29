package pe.edu.tecsup.api.tasks;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.edu.tecsup.api.remotes.apixu.OpenWeatherService;
import pe.edu.tecsup.api.remotes.apixu.WeatherApiClient;
import pe.edu.tecsup.api.remotes.apixu.models.ForecastWeather;
import pe.edu.tecsup.api.services.StudentService;
import pe.edu.tecsup.api.utils.Mailer;
import pe.edu.tecsup.api.utils.Notifier;

import javax.servlet.ServletContext;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ebenites on 16/05/2017.
 */
@Component
public class ScheduledTask {

    private static final Logger log = Logger.getLogger(ScheduledTask.class);

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private StudentService studentervice;

    @Autowired
    private Mailer mailer;

    @Autowired
    private Notifier notifier;

    @Scheduled(cron="0 * * * * *")
    public void keepalive(){
        log.info(new Date() + " - keep alive ScheduledTask ...");
    }

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    public void firsttime(){
        log.info("firsttime init ...");

        processingWeatherUpdating();

    }

    @Scheduled(cron="0 0 8 * * *")
    public void processingDealyDebts() {
        log.info("processingDealyDebts ...");
        try {

            // Instances list
            List<String> registrationIds = studentervice.getDebtorInstancesDealy();

            // Notification
            notifier.notifyPayment(registrationIds);

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

            final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            OpenWeatherService service = WeatherApiClient.getClient().create(OpenWeatherService.class);

            // https://api.apixu.com/v1/current.json?lang=es&q=Lima&key=*******************************
            // https://api.apixu.com/v1/forecast.json?lang=es&q=Lima&hour=20&key=*******************************
            ForecastWeather limaWeather = service.getWeather(WEATHER_API_KEY, "Lima", hour).execute().body();
            servletContext.setAttribute("weather-L", limaWeather);
            log.info(limaWeather);

            // https://api.apixu.com/v1/current.json?lang=es&q=Arequipa&key=*******************************
            // https://api.apixu.com/v1/forecast.json?lang=es&q=Arequipa&hour=20&key=*******************************
            ForecastWeather arequipaWeather = service.getWeather(WEATHER_API_KEY, "Arequipa", hour).execute().body();
            servletContext.setAttribute("weather-A", arequipaWeather);
            log.info(arequipaWeather);

            // https://api.apixu.com/v1/current.json?lang=es&q=Trujillo&key=*******************************
            // https://api.apixu.com/v1/forecast.json?lang=es&q=Trujillo&hour=20&key=*******************************
            ForecastWeather trujilloWeather = service.getWeather(WEATHER_API_KEY, "Trujillo", hour).execute().body();
            servletContext.setAttribute("weather-T", trujilloWeather);
            log.info(trujilloWeather);

        }catch (Exception e){
            log.error(e, e);
            mailer.sendMailToAdministrator("ERROR en ScheduledTask:processingWeatherUpdating", e.toString());
        }
    }
}
