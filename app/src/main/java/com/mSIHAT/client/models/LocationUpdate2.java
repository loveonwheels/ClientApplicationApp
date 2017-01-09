package com.mSIHAT.client.models;

import java.util.Date;

/**
 * Created by ghost on 22/8/16.
 */
public class LocationUpdate2 {

    public int hcp_id;
    public double location_lat;
    public double location_long;

    public LocationUpdate2(int hcp_id, double location_lat, double location_long) {
        this.hcp_id = hcp_id;
        this.location_lat = location_lat;
        this.location_long = location_long;
    }



}
