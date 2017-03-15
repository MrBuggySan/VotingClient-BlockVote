package com.blockvote.auxillary;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blockvote.interfaces.OnCardInteractionFragmentLeve;
import com.blockvote.votingclient.R;

/**
 * Created by Beast Mode on 3/12/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final String LOG_TAG = RVAdapter.class.getSimpleName();

    private boolean includeAddElection;
    private OnCardInteractionFragmentLeve onCardInteractionFragmentLeve;
    private Context context;

    private ElectionList elections;

    public RVAdapter(Context context, OnCardInteractionFragmentLeve onCardInteractionFragmentLeve, ElectionList elections_, boolean includeAddElection_){
        this.context=context;
        this.elections = elections_;
        this.includeAddElection = includeAddElection_;
        this.onCardInteractionFragmentLeve = onCardInteractionFragmentLeve;
    }

    /*
     0 - New election Card
     1 - Card in Ongoing list
     2 - Card in Finished list
     */
    @Override
    public int getItemViewType(int position) {
        int type;
        if(includeAddElection){
            type = (position == 0)? 2 : 1;
        }else{
            type = 0;
        }
        return type;
    }


    @Override
    public int getItemCount() {
        if(includeAddElection){
            //because of the extra new election card
            return elections.getSize() + 1;
        }
        return elections.getSize();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {

        if(type == 2){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_election_card, viewGroup, false);
            return new NewElectionViewHolder(v, onCardInteractionFragmentLeve);

        }else{
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.election_item_card, viewGroup, false);
            return new ElectionInstanceViewHolder(v, onCardInteractionFragmentLeve, includeAddElection);
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder cardEntryViewHolder, int position) {

        switch(cardEntryViewHolder.getItemViewType()){
            case 0:
                if(elections.getSize() == 0) return;
                ElectionInstanceViewHolder electionInstanceViewHolder = (ElectionInstanceViewHolder) cardEntryViewHolder;
                electionInstanceViewHolder.electionName.setText(elections.getElectionAt(position).getElectionName());
                electionInstanceViewHolder.electionURL.setText(elections.getElectionAt(position).getTimeString());
                //have different colours for the cards
//                electionInstanceViewHolder.cv.setBackgroundColor(context.getResources(CardColorPicker.NextColor(position)));
//                electionInstanceViewHolder.electionPhoto.setImageResource(elections.get(i).);
                break;

            case 1:
                if(elections.getSize() == 0) return;
                ElectionInstanceViewHolder electionInstanceViewHolder2 = (ElectionInstanceViewHolder) cardEntryViewHolder;
                electionInstanceViewHolder2.electionName.setText(elections.getElectionAt(position - 1).getElectionName());
                electionInstanceViewHolder2.electionURL.setText(elections.getElectionAt(position - 1).getTimeString());
                break;
            case 2:
                //do nothing since the new election card is already built
                break;
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ElectionInstanceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView electionName;
        TextView electionURL;
        //ImageView electionPhoto;

        ElectionInstanceViewHolder(View itemView, final OnCardInteractionFragmentLeve onCardInteractionFragmentLeve, final boolean includeAddElection) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            electionName = (TextView) itemView.findViewById(R.id.electionName);
            electionURL = (TextView) itemView.findViewById(R.id.electionTime);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int offset = (includeAddElection)? -1 : 0;
                    onCardInteractionFragmentLeve.onElectionCardPress(getAdapterPosition() + offset);

                }

            });
        }
            //electionPhoto = (ImageView)itemView.findViewById(R.id.electionPhoto);
    }

    public class NewElectionViewHolder extends RecyclerView.ViewHolder {
        NewElectionViewHolder(View itemView, final OnCardInteractionFragmentLeve onCardInteractionFragmentLeve) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCardInteractionFragmentLeve.onNewElectionCardPress();

                }

            });
        }
    }
}