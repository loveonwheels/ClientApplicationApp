package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;


    /**
     * Created by alamchristian on 3/22/16.
     */
    public class Pratitioner2 implements Parcelable {
        public int practitioner_id;
        public String practitioner_fullname;
        public String practitioner_nric;
        public String practitioner_phone;

        public Pratitioner2(){

        }

        public Pratitioner2(Parcel in){
            String[] data = new String[4];

            in.readStringArray(data);
            this.practitioner_id = Integer.parseInt(data[0]);
            this.practitioner_fullname = data[1];
            this.practitioner_nric = data[2];
            this.practitioner_phone = data[3];
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringArray(new String[]{String.valueOf(this.practitioner_id), this.practitioner_fullname,
                    this.practitioner_nric, this.practitioner_phone});
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

