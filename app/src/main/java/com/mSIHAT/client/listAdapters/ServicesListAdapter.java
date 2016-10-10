package com.mSIHAT.client.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.Service;

import java.util.ArrayList;

/**
 * Created by alamchristian on 4/15/16.
 */
public class ServicesListAdapter extends BaseAdapter {
    private ArrayList<Service> services;
    Context context;
    private static LayoutInflater inflater = null;

    public ServicesListAdapter(Context context, ArrayList<Service> services){
        this.context = context;
        this.services = services;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Object getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return services.get(position).service_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.list_row_services, null);
        }
        TextView text_service = (TextView) view.findViewById(R.id.text_list_row_services_name);
        text_service.setText(this.services.get(position).service_name);
        return view;
    }
}
