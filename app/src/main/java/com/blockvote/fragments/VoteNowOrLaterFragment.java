package com.blockvote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blockvote.interfaces.DefaultInteractions;
import com.blockvote.votingclient.R;

import static com.blockvote.votingclient.R.id.voteButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VoteNowOrLaterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VoteNowOrLaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public abstract class VoteNowOrLaterFragment extends Fragment {
    private final String LOG_TAG = VoteNowOrLaterFragment.class.getSimpleName();

    protected OnVoteNowOrLaterInteractionListener onVoteNowButtonInteraction;
    protected DefaultInteractions defaultInteractions;
    protected View rootView;

    public VoteNowOrLaterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    abstract void EditUi();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_vote_now_or_later, container, false);

        final View voteNow = rootView.findViewById(R.id.VoteNow);
        voteNow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onVoteNowButtonInteraction.onVoteNowButtonInteraction();
            }
        });

        final View voteLater = (Button) rootView.findViewById(R.id.VoteLater);
        voteLater.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onVoteNowButtonInteraction.onVoteLaterButtonInteraction();
            }
        });

        EditUi();

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVoteNowOrLaterInteractionListener &&
                context instanceof DefaultInteractions) {
            onVoteNowButtonInteraction = (OnVoteNowOrLaterInteractionListener) context;
            defaultInteractions = (DefaultInteractions) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVoteNowOrLaterInteractionListener && DefaultInteractions");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onVoteNowButtonInteraction = null;
        defaultInteractions = null;
    }

    public interface OnVoteNowOrLaterInteractionListener {
        void onVoteNowButtonInteraction();
        void onVoteLaterButtonInteraction();
    }
}
