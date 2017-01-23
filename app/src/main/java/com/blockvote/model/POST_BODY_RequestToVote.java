package com.blockvote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by andreibuiza on 22/01/17.
 */

public class POST_BODY_RequestToVote {
    @SerializedName("username")
    @Expose
    String username;

    @SerializedName("voter")
    @Expose
    String voter;


    public POST_BODY_RequestToVote(String registrarUserName, String voterName   ){
        this.username = registrarUserName;
        this.voter = voterName;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVoter() {
        return voter;
    }

    public void setVoter(String voter) {
        this.voter = voter;
    }


}