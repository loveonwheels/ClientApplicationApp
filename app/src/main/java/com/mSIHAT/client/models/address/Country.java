package com.mSIHAT.client.models.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/14/16.
 */
public class Country implements Parcelable {
    public int country_id;
    public String country_name;

    public Country(){

    }

    public Country(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.country_id = Integer.parseInt(data[0]);
        this.country_name = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(country_id), this.country_name});
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>(){

        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
