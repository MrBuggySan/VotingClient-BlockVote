package com.blockvote.model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by andreibuiza on 17/01/17.
 */

public interface TestAPIinterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    //    @Headers({"Accept: application/json"})
//    @GET("test/")
//    Call<SendDO> getHello();

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("vote/")
    Call<SendDO> getHello(@Body SendDO sendDO);





}
