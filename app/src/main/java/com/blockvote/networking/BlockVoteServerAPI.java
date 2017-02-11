package com.blockvote.networking;

import com.blockvote.model.MODEL_ElectionInfo;
import com.blockvote.model.MODEL_writeVote;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Beast Mode on 1/21/2017.
 */

public interface BlockVoteServerAPI {

    @Headers({"Accept: application/json"})
    @GET("getElectionInfo/")
    Call<MODEL_ElectionInfo> getElectionInfo();

//    @Headers({"Content-Type: application/json", "Accept: application/json"})
//    @POST("writeVote/")
//    Call<MODEL_writeVote> writeVote(@Body POST_BODY_writeVote body);

    @FormUrlEncoded
    @POST("writeVote/")
    Call<MODEL_writeVote> writeVote(@Field("region") String region, @Field("signedTokenID") String signedTokenID,
                                    @Field("signedTokenSig") String signedTokenSig ,
                                    @Field("vote") String vote,
                                    @Field("registrarName") String registrarName);
}
