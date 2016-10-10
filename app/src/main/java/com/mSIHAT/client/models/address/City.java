package com.mSIHAT.client.models.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/14/16.
 */
public class City implements Parcelable{
    public int city_id;
    public String city_name;
    public int state_id;

    public City(){

    }

    public City(Parcel in){
        String[] data = new String[3];
        in.readStringArray(data);

        this.city_id = Integer.parseInt(data[0]);
        this.city_name = data[1];
        this.state_id = Integer.parseInt(data[2]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.city_id), this.city_name,
                                String.valueOf(state_id)});
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>(){

        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
