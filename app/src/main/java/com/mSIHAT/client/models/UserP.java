package com.mSIHAT.client.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/2/16.
 */
public class UserP implements Parcelable {
    public int id;
    public String fullname;
    public String email;
    public String password;
    public String phone;
    public String creditcard;

    public UserP(){

    }
    public UserP(String fullname, String email, String password, String phone, String creditcard){
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.creditcard = creditcard;
    }
    public UserP(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        this.id = Integer.parseInt(data[0]);
        this.fullname = data[1];
        this.email = data[2];
        this.password = data[3];
        this.phone = data[4];
        this.creditcard = data[5];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {String.valueOf(this.id), this.fullname,
                                            this.email, this.password, this.phone, this.creditcard});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) {
            return new UserP(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new UserP[size];
        }
    };
}
