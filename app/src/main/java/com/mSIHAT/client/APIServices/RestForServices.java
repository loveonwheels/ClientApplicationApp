package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.utils.Constants;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alamchristian on 3/22/16.
 */
public class RestForServices {
    private retrofit2.Retrofit retrofit;
    private ServicesAPIService servicesAPI;

    public RestForServices(){
        retrofit = new retrofit2.Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        servicesAPI = retrofit.create(ServicesAPIService.class);
    }

    public ServicesAPIService getServicesAPI() { return servicesAPI; }
}
