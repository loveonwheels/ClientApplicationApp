package com.mSIHAT.client.models.Date;

import android.util.Log;


import com.mSIHAT.client.models.TimeSlots;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ghost on 13/9/16.
 */
public class TimeSlotUtil {
    public static ArrayList<TimeSlots> generateTimeSlots(int StartTime, int EndTime, int[] disabledslots){
        ArrayList<TimeSlots>  timeSlots = new ArrayList<>();
        for(int i = StartTime;i<= EndTime;i++){

            boolean found = false;
                for(int j = 0;j<disabledslots.length;j++){
                    if(i == disabledslots[j]){
                     Log.e(String.valueOf(i),String.valueOf(disabledslots[j]));
                        timeSlots.add(new TimeSlots(i,false));
                        found = true;
                        disabledslots[j] = 25;
                    }
                }

            if(!found){
                timeSlots.add(new TimeSlots(i,true));
            }

            Log.e(String.valueOf(i),String.valueOf(found));

        }


        return timeSlots;

    }

    public static ArrayList<TimeSlots> generateTimeSlots(int StartTime, int EndTime){
        ArrayList<TimeSlots>  timeSlots = new ArrayList<>();
        for(int i = StartTime;i<= EndTime;i++){


                timeSlots.add(new TimeSlots(i,true));


        }


        return timeSlots;

    }



    public static String getTimeStringValue(int timevalue){
        String timeStringvalue = "";
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
    return timeStringvalue;

    }


    public static String getDateinString(){

        Calendar c = Calendar.getInstance();

        String sDate =(c.get(Calendar.MONTH)+1)
                + "/" + c.get(Calendar.DAY_OF_MONTH)+ "/"+ c.get(Calendar.YEAR)
                ;

        return sDate;
    }

    public static String convertDateToString(java.util.Date date){

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        String sDate =(c.get(Calendar.MONTH)+1)
                + "/" + c.get(Calendar.DAY_OF_MONTH)+ "/"+ c.get(Calendar.YEAR)
                ;

        return sDate;
    }

}
