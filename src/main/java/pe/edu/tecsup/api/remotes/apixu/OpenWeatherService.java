package pe.edu.tecsup.api.remotes.apixu;

import pe.edu.tecsup.api.remotes.apixu.models.Forecast;
import pe.edu.tecsup.api.remotes.apixu.models.ForecastWeather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * https://github.com/apixu/apixu-android
 * Created by Igor Havrylyuk on 14.02.2017.
 */

public interface OpenWeatherService {

    @GET("current.json?lang=es")
    Call<ForecastWeather> getWeather(
                    @Query("key") String key,
                    @Query("q") String query);

}
