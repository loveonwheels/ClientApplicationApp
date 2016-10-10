package com.mSIHAT.client.models.views;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alamchristian on 3/31/16.
 */
public class PatientSelectionItem implements Parcelable {
    public int patient_id;
    public String patient_fullname;

    public PatientSelectionItem(){

    }

    public PatientSelectionItem(Parcel in){
        String[] data = new String[2];

        in.readStringArray(data);
        this.patient_id = Integer.parseInt(data[0]);
        this.patient_fullname = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {String.valueOf(this.patient_id), this.patient_fullname});
    }

    public static final Parcelable.Creator<PatientSelectionItem> CREATOR = new Parcelable.Creator<PatientSelectionItem>(){

        @Override
        public PatientSelectionItem createFromParcel(Parcel source) {
            return new PatientSelectionItem(source);
        }

        @Override
        public PatientSelectionItem[] newArray(int size) {
            return new PatientSelectionItem[size];
        }
    };
}
