package chotot.prect.aptech.zinzamessenger.service;

import chotot.prect.aptech.zinzamessenger.model.BodyRequest;
import chotot.prect.aptech.zinzamessenger.model.ResultRequest;
import chotot.prect.aptech.zinzamessenger.utils.Utils;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by ASUS on 02/28/2017.
 */

public interface FCMService {
    @Headers({
            "Authorization: key="+ Utils.FCM_KEY,
            "Content-Type: application/json"
    })
    @POST("/fcm/send")
    Call<ResultRequest> sendPush(@Body BodyRequest mBodyRequest);
}
