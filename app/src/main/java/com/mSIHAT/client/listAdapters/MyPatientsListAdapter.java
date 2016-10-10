package com.mSIHAT.client.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.Patient;

import java.util.List;

/**
 * Created by alamchristian on 3/3/16.
 */
public class MyPatientsListAdapter extends BaseAdapter {

    private List<Patient> patients;
    Context context;
    private static LayoutInflater inflater = null;

    public MyPatientsListAdapter(Context context, List<Patient> patients){
        this.context = context;
        this.patients = patients;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return patients.size();
    }

    @Override
    public Object getItem(int position) {
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return patients.get(position).patient_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.list_row_mypatients, null);
        }
        TextView nameText = (TextView) view.findViewById(R.id.text_list_row_mypatients_name);
        TextView nricText = (TextView) view.findViewById(R.id.text_list_row_mypatients_nric);

        nameText.setText(this.patients.get(position).patient_fullname);
        nricText.setText(this.patients.get(position).patient_nric);

        return view;
    }
}
