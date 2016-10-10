package com.mSIHAT.client.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.views.MultiAppointmentListItem;

import java.util.List;

/**
 * Created by alamchristian on 4/11/16.
 */
public class MultiAppointmentsListAdapter extends BaseAdapter {

    List<MultiAppointmentListItem> appointments = null;
    Context context;
    private static LayoutInflater inflater = null;

    public MultiAppointmentsListAdapter(Context context, List<MultiAppointmentListItem> appointments){
        this.context = context;
        this.appointments = appointments;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return appointments.get(position).multi_appointment_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.list_row_appointment_multi, null);
        }
        TextView subserviceText = (TextView) vi.findViewById(R.id.text_list_row_appointments_multi_subservice);
        TextView patientText = (TextView) vi.findViewById(R.id.text_list_row_appointments_multi_patient_name);
        TextView totalText = (TextView) vi.findViewById(R.id.text_list_row_appointments_multi_total);

        subserviceText.setText(this.appointments.get(position).subservice_name);
        patientText.setText(this.appointments.get(position).patient_fullname);
        totalText.setText(String.valueOf(
                this.appointments.get(position).multi_appointment_total
        ));
        return vi;
    }
}
