package com.blockvote.networking;

import com.blockvote.model.ElectionListModel;
import com.blockvote.model.FullElectionInfoModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

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
}
