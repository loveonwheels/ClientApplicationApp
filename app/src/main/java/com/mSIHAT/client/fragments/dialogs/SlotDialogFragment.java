package com.mSIHAT.client.fragments.dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.listAdapters.SlotListAdapter;
import com.mSIHAT.client.models.TimeSlots;


/**
 * Activities that contain this fragment must implement the
 * {@link SlotDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SlotDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlotDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_RequestCode = "param4";
    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;
    private int[] mParam3;
    private int mParam4;
    private OnFragmentInteractionListener mListener;

    public SlotDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param
     * @return A new instance of fragment SlotDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SlotDialogFragment newInstance(int requestCOde,int starttime, int endtime) {
        SlotDialogFragment fragment = new SlotDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, starttime);
        args.putInt(ARG_PARAM2, endtime);
        args.putInt(ARG_RequestCode,requestCOde);
        fragment.setArguments(args);
        return fragment;
    }

    public static SlotDialogFragment newInstance(int requestCOde,int starttime, int endtime,int[] disabledslot) {
        SlotDialogFragment fragment = new SlotDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, starttime);
        args.putInt(ARG_PARAM2, endtime);
        args.putIntArray(ARG_PARAM3, disabledslot);
        args.putInt(ARG_RequestCode,requestCOde);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View view = inflater.inflate(R.layout.fragment_slot_dialog, container, false);

        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mParam3 = getArguments().getIntArray(ARG_PARAM3);
            mParam4 = getArguments().getInt(ARG_RequestCode);
        }
        ListView listView = (ListView) view.findViewById(R.id.slots);
        final SlotListAdapter cts;
if(mParam3 == null) {
    cts= new SlotListAdapter(getActivity(), mParam1, mParam2);
}else{
    cts= new SlotListAdapter(getActivity(), mParam1, mParam2,mParam3);
}
        listView.setAdapter(cts);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(((TimeSlots)cts.getItem(position)).getEnabled()){
                    Intent intent = new Intent();
                    intent.putExtra("timevalue", ((TimeSlots)cts.getItem(position)).getTimevalue());
                    getTargetFragment().onActivityResult(getTargetRequestCode(),mParam4,intent);
                    getDialog().dismiss();

                }
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
