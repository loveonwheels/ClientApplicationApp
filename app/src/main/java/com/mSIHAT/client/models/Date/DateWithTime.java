package com.mSIHAT.client.models.Date;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by ghost on 9/1/17.
 */
public class DateWithTime {
    private String day;
    private String month;
    private String year;
    private String dayofweek;
    public Date thisdate;

    public DateWithTime(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public DateWithTime(String dat,int hours) throws ParseException {

        Log.e("value in date",dat);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal  = Calendar.getInstance();
        cal.setTime(df.parse(dat));
        this.day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        this.month = String.valueOf(cal.get(Calendar.MONTH));
        this.year = String.valueOf(cal.get(Calendar.YEAR));
        this.dayofweek = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));

        cal.add(cal.HOUR,hours);
        thisdate = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Log.e("date",dateFormat.format(thisdate.getTime()));
    }


    public long hourWithCurrent(){
                Calendar cal  = Calendar.getInstance();
                java.util.Date endDate   =  cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Log.e("date",dateFormat.format(cal.getTime()));
        Log.e("date",dateFormat.format(thisdate.getTime()));

        long duration  = endDate.getTime() - thisdate.getTime();

     //   long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
     //   long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);

        return diffInHours;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDayofweek(){

        return dayofweek;
    }
}
