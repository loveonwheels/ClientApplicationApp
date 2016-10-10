package com.mSIHAT.client.listAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mSIHAT.client.R;
import com.mSIHAT.client.models.Practitioner;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alamchristian on 4/1/16.
 */
public class AvailablePractitionersListAdapter extends BaseAdapter {

    List<Practitioner> practitioners = null;
    Context context;
    private static LayoutInflater inflater = null;

    public AvailablePractitionersListAdapter(Context context, List<Practitioner> practitioners){
        this.context = context;
        this.practitioners = practitioners;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return practitioners.size();
    }

    @Override
    public Object getItem(int position) {
        return practitioners.get(position);
    }

    @Override
    public long getItemId(int position) {
        return practitioners.get(position).hcpid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.list_row_available_practitioners, null);
        }
        TextView text_name = (TextView) view.findViewById(R.id.text_list_row_practitioner_name);
        TextView text_nric = (TextView) view.findViewById(R.id.text_list_row_practitioner_nric);
        TextView text_expertise = (TextView) view.findViewById(R.id.text_list_row_practitioner_expertise);
        TextView text_gender = (TextView) view.findViewById(R.id.text_list_row_practitioner_gender);
        TextView text_language = (TextView) view.findViewById(R.id.text_list_row_practitioner_language);
        CircularImageView searchView = (CircularImageView ) view.findViewById(R.id.text_list_row_practitioner_image);


        text_name.setText(this.practitioners.get(position).name);
        text_nric.setText(this.practitioners.get(position).level);
        text_gender.setText(this.practitioners.get(position).gender);
        text_language.setText(this.practitioners.get(position).language);

        Log.e("language",this.practitioners.get(position).language);
        text_expertise.setText(String.valueOf(this.practitioners.get(position).rating));

        Picasso.with(context)
                .load(this.practitioners.get(position).imageurl)
                .fit().centerCrop()
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(searchView);

        return view;
    }
}
