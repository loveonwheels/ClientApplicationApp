package com.mSIHAT.client.fragments.dialogs.selectors;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.listAdapters.TimeOptionsListAdapter;
import com.mSIHAT.client.models.AvailableTimeClass;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AvailableTime.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AvailableTime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailableTime extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<AvailableTimeClass> availTimes;
    ListView listView;
    private OnFragmentInteractionListener mListener;

    public AvailableTime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AvailableTime.
     */
    // TODO: Rename and change types and number of parameters
    public static AvailableTime newInstance(ArrayList<AvailableTimeClass> availableTimes) {
        AvailableTime fragment = new AvailableTime();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, availableTimes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_available_time, container, false);
        Bundle args = getArguments();
        if(args != null){
            availTimes = args.getParcelableArrayList(ARG_PARAM1);
        }

        listView = (ListView)view.findViewById(R.id.timeOptions);
        listView.setAdapter(new TimeOptionsListAdapter(getActivity().getBaseContext(), availTimes));
        //
        Log.e("opentimes value",availTimes.get(0).getStarttime());

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
