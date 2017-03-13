package com.blockvote.auxillary;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockvote.votingclient.R;

import java.util.ArrayList;

/**
 * Created by Beast Mode on 3/12/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ElectionInstanceViewHolder>{

    ArrayList<ElectionInstance> elections;

    public RVAdapter(ArrayList<ElectionInstance> elections_){
        this.elections = elections_;
    }

    @Override
    public int getItemCount() {
        return elections.size();
    }

    @Override
    public ElectionInstanceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.election_item, viewGroup, false);
        ElectionInstanceViewHolder evh = new ElectionInstanceViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(ElectionInstanceViewHolder electionInstanceViewHolder, int i) {
        electionInstanceViewHolder.electionName.setText(elections.get(i).getElectionName());
        electionInstanceViewHolder.electionURL.setText(elections.get(i).getElectionURL());
//        electionInstanceViewHolder.electionPhoto.setImageResource(elections.get(i).);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ElectionInstanceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView electionName;
        TextView electionURL;
        //ImageView electionPhoto;

        ElectionInstanceViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            electionName = (TextView)itemView.findViewById(R.id.electionName);
            electionURL = (TextView)itemView.findViewById(R.id.electionURL);
            //electionPhoto = (ImageView)itemView.findViewById(R.id.electionPhoto);
        }
    }

}