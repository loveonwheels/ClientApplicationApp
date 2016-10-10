package com.mSIHAT.client.APIServices.rxretrofit;

import com.mSIHAT.client.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alamchristian on 3/23/16.
 */
public class RestForServicesRx {
    private Retrofit retrofit;
    public ServicesAPIServiceRx servicesAPIServiceRx;

    public RestForServicesRx(){
        retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        servicesAPIServiceRx = retrofit.create(ServicesAPIServiceRx.class);
    }
}
