package com.mSIHAT.client.models.address;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/14/16.
 */
public class Postcode implements Parcelable{
    public int postcode_id;
    public String postcode_code;
    public int city_id;

    public Postcode(){

    }

    public Postcode(Parcel in){
        String[] data = new String[3];
        in.readStringArray(data);

        this.postcode_id = Integer.parseInt(data[0]);
        this.postcode_code = data[1];
        this.city_id = Integer.parseInt(data[2]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{String.valueOf(this.postcode_id), this.postcode_code,
                                    String.valueOf(this.city_id)});
    }

    public static final Parcelable.Creator<Postcode> CREATOR = new Parcelable.Creator<Postcode>(){

        @Override
        public Postcode createFromParcel(Parcel source) {
            return new Postcode(source);
        }

        @Override
        public Postcode[] newArray(int size) {
            return new Postcode[size];
        }
    };
}
