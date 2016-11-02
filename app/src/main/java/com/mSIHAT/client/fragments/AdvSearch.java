package com.mSIHAT.client.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.listAdapters.PartialAvailablePractitionersListAdapter;
import com.mSIHAT.client.models.PractitionerPartial;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdvSearch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdvSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdvSearch extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int timereq;
    private String searchDate;
    private int frequency;

    public List<PractitionerPartial> practitioners;

    private OnFragmentInteractionListener mListener;

    public AdvSearch() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AdvSearch newInstance(ArrayList pracList, String DateReq,int timeReq,int frequency) {
        AdvSearch fragment = new AdvSearch();
        Bundle args = new Bundle();
        args.putParcelableArrayList("pracList", pracList);
        args.putString("Date", DateReq);
        args.putInt("timereq", timeReq);
        args.putInt("frq", frequency);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

           timereq = getArguments().getInt("timereq");
            searchDate = getArguments().getString("Date");
            frequency = getArguments().getInt("frq");
          practitioners = getArguments().getParcelableArrayList("pracList");


        }
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.alerttheme2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_adv_search, container, false);
        getDialog().requestWindowFeature(STYLE_NO_TITLE);

ListView listview = (ListView)view.findViewById(R.id.partialList);
        PartialAvailablePractitionersListAdapter thelist = new PartialAvailablePractitionersListAdapter(getContext(),practitioners);
        listview.setAdapter(thelist);
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


    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
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
