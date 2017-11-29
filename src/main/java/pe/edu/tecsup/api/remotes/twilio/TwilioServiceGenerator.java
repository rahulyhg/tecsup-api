package pe.edu.tecsup.api.remotes.twilio;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * https://www.apixu.com/my/
 * Created by Igor Havrylyuk on 14.02.2017.
 */

public class TwilioServiceGenerator {

    public static final String BASE_WEATHER_URL = "https://api.authy.com/";

    private static Retrofit sRetrofit = null;

    private TwilioServiceGenerator() {
    }

    public static Retrofit getClient() {
        if (sRetrofit == null) {
            synchronized (Retrofit.class) {
                if (sRetrofit == null) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                    sRetrofit = new Retrofit.Builder()
                            .baseUrl(TwilioServiceGenerator.BASE_WEATHER_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client)
                            .build();
                }
            }
        }
        return sRetrofit;
    }

}