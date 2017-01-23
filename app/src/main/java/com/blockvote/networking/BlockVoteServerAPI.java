package com.blockvote.networking;

import com.blockvote.model.MODEL_ElectionList;
import com.blockvote.model.MODEL_ElectionInfo;
import com.blockvote.model.POST_BODY_RequestToVote;
import com.blockvote.model.MODEL_RequestToVote;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Beast Mode on 1/21/2017.
 */

public interface BlockVoteServerAPI {

    @Headers({"Accept: application/json"})
    @GET("elections/")
    Call<MODEL_ElectionList> getElectionList();

    @Headers({"Accept: application/json"})
        @GET("getElectionInfo/")
    Call<MODEL_ElectionInfo> getElectionInfo();

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("requestToVote/")
    Call<MODEL_RequestToVote> sendRegistrationRequest(@Body POST_BODY_RequestToVote postRequestToVoteBody);
}
