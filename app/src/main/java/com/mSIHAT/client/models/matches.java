package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ghost on 25/10/16.
 */
public class matches implements Parcelable{
    public int conmatches;
    public String startdate;

    protected matches(Parcel in) {
        conmatches = in.readInt();
        startdate = in.readString();
    }

    public static final Creator<matches> CREATOR = new Creator<matches>() {
        @Override
        public matches createFromParcel(Parcel in) {
            return new matches(in);
        }

        @Override
        public matches[] newArray(int size) {
            return new matches[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(conmatches);
        dest.writeString(startdate);
    }
}
