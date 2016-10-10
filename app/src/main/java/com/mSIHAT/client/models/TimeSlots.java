package com.mSIHAT.client.models;

/**
 * Created by ghost on 13/9/16.
 */
public class TimeSlots {
    public int getTimevalue() {
        return timevalue;
    }

    public void setTimevalue(int timevalue) {
        this.timevalue = timevalue;

    }

    public int timevalue;

    public String getTimeStringvalue() {
        return timeStringvalue;
    }

    public void setTimeStringvalue(String timeStringvalue) {
        this.timeStringvalue = timeStringvalue;
    }

    public String timeStringvalue;


    public boolean getEnabled() {
        return Enabled;
    }

    public void setEnabled(boolean enabled) {
        Enabled = enabled;
    }

    public boolean Enabled;
    public TimeSlots(int value,boolean enabled){
        timevalue = value;
        Enabled = enabled;
        if(timevalue == 0){
            timeStringvalue = "12:00 AM";
        }else if(timevalue == 12){
            timeStringvalue = "12:00 PM";
        }else if(timevalue == 24){
            timeStringvalue = "12:00 AM";
        }else if(timevalue >0 && timevalue <10){
            timeStringvalue = "0"+String.valueOf(timevalue)+":00 AM";

        }else if(timevalue >9 && timevalue <13){
            timeStringvalue = String.valueOf(timevalue)+":00 AM";
        }else{

            if((timevalue-12) <10){
                timeStringvalue = "0"+String.valueOf(timevalue-12)+":00 PM";

            }else
            timeStringvalue = String.valueOf(timevalue-12)+":00 PM";
        }
    }

}
