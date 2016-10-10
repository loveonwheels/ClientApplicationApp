package com.mSIHAT.client.models;

/**
 * Created by alamchristian on 4/16/16.
 */
public class Feedback {
    public int feedback_id;
    public int appointment_id;
    public int rating;
    public String comment;

    public Feedback(){

    }

    public Feedback(int appointment_id, int rating, String comment){
        this.appointment_id = appointment_id;
        this.rating = rating;
        this.comment = comment;
    }
}
