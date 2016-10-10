package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/22/16.
 */
public class Practitioner implements Parcelable{

    public int hcpid;
    public int rating;
    public String imageurl;
    public String name;
    public String phonenumber;
    public String gender;
    public String language;
    public String level;


    public Practitioner(){

    }

    public Practitioner(Parcel in){
            String[] data = new String[8];

            in.readStringArray(data);
            this.hcpid = Integer.parseInt(data[0]);
            this.rating = Integer.parseInt(data[1]);
            this.imageurl = data[2];
            this.name = data[3];
            this.phonenumber = data[4];
            this.gender = data[5];
            this.language = data[6];
            this.level = data[7];
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(new String[]{String.valueOf(this.hcpid),String.valueOf(this.rating)
                    ,this.imageurl,this.name,this.phonenumber,
                    this.gender,
                    this.language, this.level});
        }

        public static final Parcelable.Creator<Practitioner> CREATOR = new Parcelable.Creator<Practitioner>(){

            @Override
            public Practitioner createFromParcel(Parcel source) {
                return new Practitioner(source);
            }

            @Override
            public Practitioner[] newArray(int size) {
                return new Practitioner[size];
            }
        };
}
