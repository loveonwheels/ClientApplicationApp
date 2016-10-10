package com.mSIHAT.client.models.views;
import android.content.Context;

import com.mSIHAT.client.R;

/**
 * Created by alamchristian on 4/1/16.
 */
public class PractitionerAvailable {
    public int practitioner_id;
    public String practitioner_fullname;
    public String practitioner_nric;
    public String practitioner_expertise;

    private Context context;

    public PractitionerAvailable(){

    }

    public PractitionerAvailable(Context context, int id, String fullname, String nric, int expertise){
        this.context = context;
        this.practitioner_id = id;
        this.practitioner_fullname = fullname;
        this.practitioner_nric = nric;
        switch (expertise){
            case 1:
                this.practitioner_expertise = context.getString(R.string.beginner);

                break;
            case 2:
                this.practitioner_expertise = context.getString(R.string.intermediate);
                break;
            case 3:
                this.practitioner_expertise = context.getString(R.string.expert);
                break;
            default:
                break;
        }
    }
}
