package com.mSIHAT.client.models.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/14/16.
 */
public class State implements Parcelable{
    public int state_id;
    public String state_name;
    public int country_id;

    public State(){

    }

    public State (Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.state_id = Integer.parseInt(data[0]);
        this.state_name = data[1];
        this.country_id = Integer.parseInt(data[2]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.state_id), this.state_name,
                                String.valueOf(country_id)});
    }

    public static final Parcelable.Creator<State> CREATOR = new Parcelable.Creator<State>(){

        @Override
        public State createFromParcel(Parcel source) {
            return new State();
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };
}
