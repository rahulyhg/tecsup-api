package pe.edu.tecsup.api.remotes.apixu;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * https://www.apixu.com/my/
 * Created by Igor Havrylyuk on 14.02.2017.
 */

public class WeatherApiClient {

    public static final String BASE_WEATHER_URL = "http://api.apixu.com/v1/";

    private static Retrofit sRetrofit = null;

    private WeatherApiClient() {
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