package com.mSIHAT.client.listAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mSIHAT.client.R;
import com.mSIHAT.client.models.TimeSlotUtil;
import com.mSIHAT.client.models.TimeSlots;

import java.util.ArrayList;

/**
 * Created by Administrator on 14/11/2015.
 */
public class SlotListAdapter extends BaseAdapter {
    private ArrayList<TimeSlots> listData;
    private LayoutInflater layoutInflater;

    public SlotListAdapter(Context aContext, int Starttime, int endtime) {
        listData = TimeSlotUtil.generateTimeSlots(Starttime,endtime);
        layoutInflater = LayoutInflater.from(aContext);
    }

    public SlotListAdapter(Context aContext, int Starttime, int endtime, int[] disabledslots) {
        listData = TimeSlotUtil.generateTimeSlots(Starttime,endtime,disabledslots);
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
       convertView = layoutInflater.inflate(R.layout.list_schedueslotitem, null);


            holder.slotText = (TextView) convertView.findViewById(R.id.slottext);
            holder.slotbk = (RelativeLayout) convertView.findViewById(R.id.slotbk);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.slotText.setText( listData.get(position).getTimeStringvalue());

        if(!listData.get(position).getEnabled()){
            holder.slotbk.setBackgroundColor(Color.parseColor("#ffd6d7d7"));
        }else{

            holder.slotbk.setBackgroundColor(Color.parseColor("#7b0a5e"));
        }

        return convertView;
    }


    static class ViewHolder {
        TextView slotText;
        RelativeLayout slotbk;

    }
}