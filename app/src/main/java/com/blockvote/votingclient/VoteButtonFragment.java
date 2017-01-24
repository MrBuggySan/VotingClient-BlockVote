package com.blockvote.votingclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Iterator;
import java.util.Set;


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

    private OnFragmentInteractionListener mListener;

    private String[] optionList;

    public VoteButtonFragment() {
        // Required empty public constructor
    }


    public static VoteButtonFragment newInstance(Set<String> option_) {
        String[] optionArray = new String[option_.size()];
        Iterator<String> iter = option_.iterator();
        byte i = 0;
        while(iter.hasNext()){
            optionArray[i] = iter.next();

            iter.remove();
            i++;
        }
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM1,optionArray);
        VoteButtonFragment fragment = new VoteButtonFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            optionList = getArguments().getStringArray(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_vote_button, container, false);

        for(int i = 0 ; i < optionList.length; i ++){
            Log.v(LOG_TAG, "Option: " + optionList[i]);
        }
        final Button voteButton = (Button) rootView.findViewById(R.id.voteButton);
        voteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onVoteButtonInteraction(optionList);
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
        void onVoteButtonInteraction(String[] optionList);
    }
}
