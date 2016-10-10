package com.mSIHAT.client.APIServices.rxretrofit;

import com.mSIHAT.client.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alamchristian on 3/22/16.
 */
public class RestAppointmentServiceRx {
    private Retrofit retrofit;
    public AppointmentServiceRx appointmentService;

    public RestAppointmentServiceRx(){
        retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        appointmentService = retrofit.create(AppointmentServiceRx.class);
    }
}
