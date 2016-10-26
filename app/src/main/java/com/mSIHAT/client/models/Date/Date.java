package com.mSIHAT.client.models.Date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ghost on 1/2/16.
 */
public class Date {
    private String day;
    private String month;
    private String year;
    private String dayofweek;

    public Date(String day, String month, String year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Date(String dat) throws ParseException {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal  = Calendar.getInstance();
        cal.setTime(df.parse(dat));
        this.day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        this.month = String.valueOf(cal.get(Calendar.MONTH));
        this.year = String.valueOf(cal.get(Calendar.YEAR));
        this.dayofweek = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
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
