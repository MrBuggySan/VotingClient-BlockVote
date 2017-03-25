package com.blockvote.auxillary;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blockvote.interfaces.OnCardInteractionFragmentLeve;
import com.blockvote.votingclient.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Beast Mode on 3/12/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final String LOG_TAG = RVAdapter.class.getSimpleName();

    private boolean includeAddElection;
    private OnCardInteractionFragmentLeve onCardInteractionFragmentLeve;
    private final Context context;

    private ElectionList elections;

    public RVAdapter(Context context, OnCardInteractionFragmentLeve onCardInteractionFragmentLeve, ElectionList elections_, boolean includeAddElection_){
        this.context=context;
        this.elections = elections_;
        this.includeAddElection = includeAddElection_;
        this.onCardInteractionFragmentLeve = onCardInteractionFragmentLeve;
    }

    /*
     2 - New election Card
     1 - Card in Ongoing list
     0 - Card in Finished list
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
                ElectionInstance electionInstance = elections.getElectionAt(position);
                ElectionInstanceViewHolder electionInstanceViewHolder = (ElectionInstanceViewHolder) cardEntryViewHolder;
                electionInstanceViewHolder.electionName.setText(electionInstance.getElectionName());
                electionInstanceViewHolder.timeString.setText(electionInstance.getTimeString());
                int id = electionInstance.getId();
                electionInstanceViewHolder.id = id;
                //have different colours for the cards
                ImageView imageView = electionInstanceViewHolder.electionImg;
                electionInstanceViewHolder.cv.setBackgroundColor(context.getResources().getColor(CardColorPicker.NextColor(id)));
                Picasso.with(context)
                        .load(electionInstance.getElectionFlagURL())
                        .into(imageView);
                break;

            case 1:
                if(elections.getSize() == 0) return;
                ElectionInstance electionInstance2 = elections.getElectionAt(position - 1);
                ElectionInstanceViewHolder electionInstanceViewHolder2 = (ElectionInstanceViewHolder) cardEntryViewHolder;
                electionInstanceViewHolder2.electionName.setText(electionInstance2.getElectionName());
                electionInstanceViewHolder2.timeString.setText(electionInstance2.getTimeString());
                int id2 = electionInstance2.getId();
                electionInstanceViewHolder2.id = id2;
                electionInstanceViewHolder2.cv.setBackgroundColor(context.getResources().getColor(CardColorPicker.NextColor(id2)));
                ImageView imageView2 = electionInstanceViewHolder2.electionImg;
                Picasso.with(context)
                        .load(electionInstance2.getElectionFlagURL())
                        .into(imageView2);
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

    public void removeAt(int id, int position) {
        int realposition = (includeAddElection)? position - 1: position;
        Log.d(LOG_TAG, "item with id " + id + " and position " + realposition + " needs to be removed");
        elections.deleteElection(id);
        notifyItemRemoved(realposition);
        notifyItemRangeChanged(realposition, elections.getSize());

        //Update the electionlist in dataStore
        onCardInteractionFragmentLeve.onElectionDelete(id);

    }

    public class ElectionInstanceViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        CardView cv;
        TextView electionName;
        TextView timeString;
        ImageView electionImg;
        ImageButton deleteButton;
        int id;

        ElectionInstanceViewHolder(View itemView, final OnCardInteractionFragmentLeve onCardInteractionFragmentLeve, final boolean includeAddElection) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            electionName = (TextView) itemView.findViewById(R.id.electionName);
            timeString = (TextView) itemView.findViewById(R.id.electionTime);
            electionImg = (ImageView) itemView.findViewById(R.id.electionImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCardInteractionFragmentLeve.onElectionCardPress(id);
                }

            });

            deleteButton = (ImageButton) itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            if(v.equals(deleteButton)){
                removeAt(id, getAdapterPosition());
            }
        }
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