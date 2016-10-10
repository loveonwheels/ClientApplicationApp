package com.mSIHAT.client.listAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.AvailableTimeClass;

import java.util.List;

/**
 * Created by alamchristian on 4/1/16.
 */
public class TimeOptionsListAdapter extends BaseAdapter {

    List<AvailableTimeClass> availableTimes = null;
    Context context;
    private static LayoutInflater inflater = null;

    public TimeOptionsListAdapter(Context context, List<AvailableTimeClass> availableTimes){
        this.context = context;
        this.availableTimes = availableTimes;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return availableTimes.size();
    }

    @Override
    public Object getItem(int position) {
        return availableTimes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.list_row_available_practitioners_suggest_time, null);
        }
        TextView text_name = (TextView) view.findViewById(R.id.textViewOptionTime);


        text_name.setText(this.availableTimes.get(position).getStarttime()+" - "+this.availableTimes.get(position).getEndtime());


        return view;
    }
}
