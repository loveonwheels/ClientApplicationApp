package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by alamchristian on 3/22/16.
 */
public class PractitionerAvail implements Parcelable{

    public int hcpid;
    public int rating;
    public String imageurl;
    public String name;
    public String phonenumber;
    public String gender;
    public String language;
    public String level;
    public int starttime;
    public int endtime;
    public List<Integer> unavailableslot;
    public List<String> availabledate;

    public PractitionerAvail(){

    }


    protected PractitionerAvail(Parcel in) {
        hcpid = in.readInt();
        rating = in.readInt();
        imageurl = in.readString();
        name = in.readString();
        phonenumber = in.readString();
        gender = in.readString();
        language = in.readString();
        level = in.readString();
        starttime = in.readInt();
        endtime = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(hcpid);
        dest.writeInt(rating);
        dest.writeString(imageurl);
        dest.writeString(name);
        dest.writeString(phonenumber);
        dest.writeString(gender);
        dest.writeString(language);
        dest.writeString(level);
        dest.writeInt(starttime);
        dest.writeInt(endtime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PractitionerAvail> CREATOR = new Creator<PractitionerAvail>() {
        @Override
        public PractitionerAvail createFromParcel(Parcel in) {
            return new PractitionerAvail(in);
        }

        @Override
        public PractitionerAvail[] newArray(int size) {
            return new PractitionerAvail[size];
        }
    };
}
