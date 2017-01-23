package com.blockvote.networking;

import com.blockvote.model.ElectionListModel;
import com.blockvote.model.FullElectionInfoModel;
import com.blockvote.model.POSTRequestToVoteBody;
import com.blockvote.model.SendDO;

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
    Call<ElectionListModel> getElectionList();

    @Headers({"Accept: application/json"})
    @GET("getElectionInfo/")
    Call<FullElectionInfoModel> getElectionInfo();

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("requestToVote/")
    Call<> sendRegistrationRequest(@Body POSTRequestToVoteBody postRequestToVoteBody);
}
