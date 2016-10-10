package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alamchristian on 3/23/16.
 */
public class RestAppointmentService {
    private Retrofit retrofit;
    private AppointmentService appointmentService;

    public RestAppointmentService(){
        retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        appointmentService = retrofit.create(AppointmentService.class);
    }

    public AppointmentService getService() { return appointmentService; }
}
