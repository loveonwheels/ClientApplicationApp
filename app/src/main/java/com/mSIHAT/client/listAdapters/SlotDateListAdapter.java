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
import com.mSIHAT.client.models.Date.DateFormatter;
import com.mSIHAT.client.models.TimeSlotUtil;
import com.mSIHAT.client.models.TimeSlots;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Administrator on 14/11/2015.
 */
public class SlotDateListAdapter extends BaseAdapter {
    private ArrayList<String> listData;
    private LayoutInflater layoutInflater;

    public SlotDateListAdapter(Context aContext,ArrayList<String> dates) {
        listData = dates;
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
       convertView = layoutInflater.inflate(R.layout.list_schedueslotitem_date, null);


            holder.slotText = (TextView) convertView.findViewById(R.id.slottext);
            holder.slotbk = (RelativeLayout) convertView.findViewById(R.id.slotbk);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        DateFormatter dateForm;
        try {
         dateForm = new DateFormatter(listData.get(position));
            holder.slotText.setText(dateForm.getDateWithDay());
        } catch (ParseException e) {
            e.printStackTrace();
        }



        /*
        if(!listData.get(position).getEnabled()){
            holder.slotbk.setBackgroundColor(Color.parseColor("#ffd6d7d7"));
        }else{

            holder.slotbk.setBackgroundColor(Color.parseColor("#7b0a5e"));
        }
        */

        return convertView;
    }


    static class ViewHolder {
        TextView slotText;
        RelativeLayout slotbk;

    }
}