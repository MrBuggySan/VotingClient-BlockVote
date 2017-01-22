package com.blockvote.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Beast Mode on 1/21/2017.
 */

public class BlockVoteServerInstance {
    private final String BASE_URL = "https://blockvotenode2.mybluemix.net/";
    private BlockVoteServerAPI apiService;

    public BlockVoteServerInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(BlockVoteServerAPI.class);
    }

    public BlockVoteServerAPI getAPI(){
        return apiService;
    }

}
