package com.mSIHAT.client.models;

/**
 * Created by alamchristian on 4/5/16.
 */
public class ConditionPost {
    public int appointment_condition_id;
    public int appointment_id;
    public String mobility;

    public ConditionPost(){

    }

    public ConditionPost(String[] condition_ids){
        this.mobility = condition_ids[0];
    }
}
