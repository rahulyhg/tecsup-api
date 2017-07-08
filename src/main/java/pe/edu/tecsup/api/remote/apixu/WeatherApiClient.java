package pe.edu.tecsup.api.remote.apixu;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by Igor Havrylyuk on 14.02.2017.
 */

public class WeatherApiClient {

    public static final String BASE_WEATHER_URL = "http://api.apixu.com/v1/";
    public static final String WEATHER_API_KEY = "543a338148814597a21235831172906";

    private static Retrofit sRetrofit = null;

    public WeatherApiClient() {
    }

    public static Retrofit getClient() {
        if (sRetrofit == null) {
            synchronized (Retrofit.class) {
                if (sRetrofit == null) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                    sRetrofit = new Retrofit.Builder()
                            .baseUrl(WeatherApiClient.BASE_WEATHER_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }
        return sRetrofit;
    }

}