package pe.edu.tecsup.api.controllers.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.edu.tecsup.api.models.Weather;
import pe.edu.tecsup.api.remotes.apixu.models.ForecastWeather;

import javax.servlet.ServletContext;

@Controller
@RequestMapping("/api/weather")
public class WeatherController {

	private static final Logger log = Logger.getLogger(WeatherController.class);

    @Autowired
    private ServletContext servletContext;

    @GetMapping("/{seat}")
    public ResponseEntity<?> getWeather(@PathVariable String seat) throws Exception{
        log.info("call getWheather: seat:" + seat);
        try {

            ForecastWeather weatherModel = (ForecastWeather) servletContext.getAttribute("weather-"+seat);
            log.info("weatherModel: " + weatherModel);

            if(weatherModel == null)
                throw new Exception("Información del clima no disponible");

            Weather weather = new Weather();
            weather.setLocation(weatherModel.getLocation().getName());
            weather.setDate(weatherModel.getCurrent().getLastUpdated());
            weather.setTemperature(weatherModel.getCurrent().getTempC());
            weather.setHumidity(weatherModel.getCurrent().getHumidity());
            weather.setCondition(weatherModel.getCurrent().getCondition().getText());
            weather.setIcon(weatherModel.getCurrent().getCondition().getIcon());

            return ResponseEntity.ok(weather);
        }catch (Throwable e){
            log.error(e, e);
            throw e;
        }
    }

}
