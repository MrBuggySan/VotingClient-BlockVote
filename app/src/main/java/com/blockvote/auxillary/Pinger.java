package com.blockvote.auxillary;

import com.blockvote.model.MODEL_getRegistrarInfo;
import com.blockvote.networking.BlockVoteServerAPI;
import com.blockvote.networking.BlockVoteServerInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dell-Laptop on 2017-03-14.
 */

public  class Pinger {

    public static void PingServer(String electionURL){
        BlockVoteServerInstance blockVoteServerInstance = new BlockVoteServerInstance(electionURL);
        BlockVoteServerAPI apiService = blockVoteServerInstance.getAPI();
        Call<String> call = apiService.getAuthPing();
        call.enqueue((new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //TODO: send a success
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //TODO:send a success
            }
        }));
    }
}
