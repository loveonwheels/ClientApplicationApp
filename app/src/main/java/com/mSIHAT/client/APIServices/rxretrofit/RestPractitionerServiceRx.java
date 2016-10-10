package com.mSIHAT.client.APIServices.rxretrofit;

import com.mSIHAT.client.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alamchristian on 3/29/16.
 */
public class RestPractitionerServiceRx {
    private Retrofit retrofit;
    public PractitionerServiceRx service;

    public RestPractitionerServiceRx(){
        retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service = retrofit.create(PractitionerServiceRx.class);
    }
}
