package pe.edu.tecsup.api.remotes.twilio;

import pe.edu.tecsup.api.remotes.twilio.models.ResponseChecking;
import pe.edu.tecsup.api.remotes.twilio.models.ResponseVerification;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * https://www.twilio.com/docs/api/authy/authy-phone-verification-api
 * Created by Igor Havrylyuk on 14.02.2017.
 */

public interface TwilioService {

    @FormUrlEncoded
    @POST("protected/json/phones/verification/start")
    Call<ResponseVerification> startPhoneVerification(
            @Field("api_key") String key,
            @Field("via") String via,
            @Field("country_code") String countrycode,
            @Field("phone_number") String phonenumber);

    @GET("protected/json/phones/verification/check")
    Call<ResponseChecking> checkPhoneVerification(
            @Query("api_key") String key,
            @Query("country_code") String countrycode,
            @Query("phone_number") String phonenumber,
            @Query("verification_code") String code);

}
