package com.vendemas.vendemas.utilities.mensajeria.remoto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import com.vendemas.vendemas.utilities.mensajeria.moldes.Sender;
import com.vendemas.vendemas.utilities.mensajeria.moldes.mResponse;

/**
 * Created by RicK' on 27/02/2018.
 */

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAANUxw4PQ:APA91bEhlps2JbpmJpiw5JsSmoen4Vxeg1Rp1BChuvJMCfzEUDaaSzRr4eREsZXt2S1O8z3nLWDTEu7talSeRHpZ67rymbXcx30gbUIEfDuMjsEn0U1D1b2o6ogj8A3CqrRrTfFYpL3T"
    })
    @POST("fcm/send")
    Call<mResponse> sendNotification( @Body Sender body);

}
