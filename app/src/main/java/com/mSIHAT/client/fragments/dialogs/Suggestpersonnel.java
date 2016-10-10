package com.mSIHAT.client.fragments.dialogs;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.AvailablePractitionersFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Suggestpersonnel.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Suggestpersonnel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Suggestpersonnel extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Button cancel,btn_differentTime,btn_differentDate;

    private OnFragmentInteractionListener mListener;

    public Suggestpersonnel() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Suggestpersonnel.
     */
    // TODO: Rename and change types and number of parameters
    public static Suggestpersonnel newInstance(String param1, String param2) {
        Suggestpersonnel fragment = new Suggestpersonnel();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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


    setStyle(DialogFragment.STYLE_NO_TITLE,
             android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setStyle(DialogFragment.STYLE_NO_TITLE,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
        View view = inflater.inflate(R.layout.fragment_suggestpersonnel, container, false);
        AvailablePractitionersFragment frag = (AvailablePractitionersFragment) getTargetFragment();
        cancel = (Button)view.findViewById(R.id.button6);
        btn_differentDate = (Button)view.findViewById(R.id.button5);
        btn_differentTime = (Button)view.findViewById(R.id.button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        btn_differentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(frag != null){
                    frag.searchfordifferenttime();
                }

                dismiss();
            }
        });


        btn_differentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(frag != null){
                    frag.searchfordifferentdate();
                }
                dismiss();
            }
        });




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
