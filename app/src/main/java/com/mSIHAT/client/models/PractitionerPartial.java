package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghost on 25/10/16.
 */
public class PractitionerPartial  implements Parcelable {

    public int hcpid;
    public int rating;
    public String imageurl;
    public String name;
    public String phonenumber;
    public String gender;
    public String language;
    public String level;
    public List<matches> matches;
    public int highestcon;
    public int totalmatches;

    public PractitionerPartial() {

    }

    protected PractitionerPartial(Parcel in) {
        hcpid = in.readInt();
        rating = in.readInt();
        imageurl = in.readString();
        name = in.readString();
        phonenumber = in.readString();
        gender = in.readString();
        language = in.readString();
        level = in.readString();
        matches = in.createTypedArrayList(com.mSIHAT.client.models.matches.CREATOR);
        highestcon = in.readInt();
        totalmatches = in.readInt();
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
        dest.writeTypedList(matches);
        dest.writeInt(highestcon);
        dest.writeInt(totalmatches);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PractitionerPartial> CREATOR = new Creator<PractitionerPartial>() {
        @Override
        public PractitionerPartial createFromParcel(Parcel in) {
            return new PractitionerPartial(in);
        }

        @Override
        public PractitionerPartial[] newArray(int size) {
            return new PractitionerPartial[size];
        }
    };
}
