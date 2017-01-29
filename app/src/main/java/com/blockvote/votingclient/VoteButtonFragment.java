package com.blockvote.votingclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VoteButtonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VoteButtonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoteButtonFragment extends Fragment {
    private final String LOG_TAG = VoteButtonFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private String voterName;

    public VoteButtonFragment() {
        // Required empty public constructor
    }


    public static VoteButtonFragment newInstance(String voterName_) {
        Bundle args = new Bundle();
        VoteButtonFragment fragment = new VoteButtonFragment();
        args.putString(ARG_PARAM2,voterName_);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voterName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vote_button, container, false);

        TextView blurb = (TextView)rootView.findViewById(R.id.voteBut_blurb);
        blurb.setText(voterName +", you can now vote.");
        final Button voteButton = (Button) rootView.findViewById(R.id.voteButton);
        voteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onVoteButtonInteraction();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onVoteButtonInteraction();
    }
}
