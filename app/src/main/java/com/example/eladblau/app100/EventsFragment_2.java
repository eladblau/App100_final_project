package com.example.eladblau.app100;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.example.eladblau.app100.EventsFragment_2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.example.eladblau.app100.EventsFragment_2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment_2 extends Fragment {

    private static final String EVENT_CAR_THIEF_NAME = new String("Car theft");
    private static final String EVENT_OTHER_NAME = new String("Other");

    private OnFragmentInteractionListener mListener;

    private ImageButton carTheftImageButton;
    private Button otherButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment_2 newInstance(String param1, String param2) {
        EventsFragment_2 fragment = new EventsFragment_2();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public EventsFragment_2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          //  mParam1 = getArguments().getString(ARG_PARAM1);
           // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.events_2_list_layout,
                container, false);


        carTheftImageButton = (ImageButton) rootView.findViewById(R.id.carTheftImageButton);
        carTheftImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EB","onClick in EventsFragment2");
                mListener.onEventButtonClicked(config.EVENT_CODE_CAR_THEFT, EVENT_CAR_THIEF_NAME);
            }
        });
        otherButton = (Button) rootView.findViewById(R.id.otherButton);
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("EB","onClick in EventsFragment2");
                mListener.onEventButtonClicked(config.EVENT_CODE_OTHER, EVENT_OTHER_NAME);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mListener = (OnFragmentInteractionListener) activity;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        public void onEventButtonClicked(int eventNumber, String eventName);
    }

}
