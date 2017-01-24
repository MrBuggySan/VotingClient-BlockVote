package com.blockvote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Beast Mode on 1/23/2017.
 */

public class POST_BODY_writeVote {
    @SerializedName("username")
    @Expose
    String username;

    @SerializedName("voter")
    @Expose
    String voter;

    @SerializedName("vote")
    @Expose
    String vote;

    @SerializedName("district")
    @Expose
    String district;

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public POST_BODY_writeVote(String registrarUserName, String voterName, String choice, String district_   ){
        this.username = registrarUserName;
        this.voter = voterName;
        this.vote = choice;
        this.district = district_;

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
