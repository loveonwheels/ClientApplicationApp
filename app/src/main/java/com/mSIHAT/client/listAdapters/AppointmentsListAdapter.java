package com.mSIHAT.client.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.views.AppointmentListItem;

import java.util.List;

/**
 * Created by alamchristian on 2/20/16.
 */
public class AppointmentsListAdapter extends BaseAdapter {

    List<AppointmentListItem> appointments = null;
    Context context;
    private static LayoutInflater inflater = null;

    public AppointmentsListAdapter(Context context, List<AppointmentListItem> appointments) {
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
        return appointments.get(position).appointment_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null) {
            vi = inflater.inflate(R.layout.list_row_appointments, null);
        }
        TextView subserviceText = (TextView) vi.findViewById(R.id.text_list_row_appointments_subservice);
        TextView patientText = (TextView) vi.findViewById(R.id.text_list_row_appointments_patient_name);
        TextView dateText = (TextView) vi.findViewById(R.id.text_list_row_appointments_date);
        TextView timeText = (TextView) vi.findViewById(R.id.text_list_row_appointments_time);
        TextView dayText = (TextView) vi.findViewById(R.id.text_list_row_day);

        subserviceText.setText(this.appointments.get(position).subservice_name);
        patientText.setText(this.appointments.get(position).patient_fullname);
        dateText.setText(this.appointments.get(position).appointment_date);
        timeText.setText(this.appointments.get(position).appointment_time);
      dayText.setText(this.appointments.get(position).appointment_day);


        return vi;
    }
}
