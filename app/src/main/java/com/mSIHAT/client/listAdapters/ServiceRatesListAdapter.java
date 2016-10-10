package com.mSIHAT.client.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.ServiceRate;

import java.util.List;

/**
 * Created by alamchristian on 4/15/16.
 */
public class ServiceRatesListAdapter extends BaseAdapter {
    private List<ServiceRate> rates;
    Context context;
    private static LayoutInflater inflater = null;

    public ServiceRatesListAdapter(Context context, List<ServiceRate> rates){
        this.context = context;
        this.rates = rates;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return rates.size();
    }

    @Override
    public Object getItem(int position) {
        return rates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rates.get(position).rate_id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.list_row_service_rates, null);
        }
        TextView text_rate = (TextView) view.findViewById(R.id.text_list_row_rate);
        TextView text_expertise = (TextView) view.findViewById(R.id.text_list_row_expertise_level);

        text_rate.setText(String.valueOf(this.rates.get(position).rate));
        String expertise = null;
        switch (rates.get(position).expertise_level){
            case 1:
                expertise = context.getString(R.string.beginner);
                break;
            case 2:
                expertise = context.getString(R.string.intermediate);
                break;
            case 3:
                expertise = context.getString(R.string.expert);
                break;
            default:
                break;
        }
        text_expertise.setText(expertise);
        return view;
    }
}
