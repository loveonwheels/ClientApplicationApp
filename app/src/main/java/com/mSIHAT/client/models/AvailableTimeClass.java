package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ghost on 6/9/16.
 */
public class AvailableTimeClass implements Parcelable {
    public String starttime;

    protected AvailableTimeClass(Parcel in) {
        starttime = in.readString();
        endtime = in.readString();
    }

    public static final Creator<AvailableTimeClass> CREATOR = new Creator<AvailableTimeClass>() {
        @Override
        public AvailableTimeClass createFromParcel(Parcel in) {
            return new AvailableTimeClass(in);
        }

        @Override
        public AvailableTimeClass[] newArray(int size) {
            return new AvailableTimeClass[size];
        }
    };

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String endtime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(starttime);
        dest.writeString(endtime);
    }
}
