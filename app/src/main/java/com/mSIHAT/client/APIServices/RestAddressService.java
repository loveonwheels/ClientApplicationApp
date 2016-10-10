package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.utils.Constants;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alamchristian on 3/14/16.
 */
public class RestAddressService {

    private retrofit2.Retrofit retrofit;
    private AddressService addressService;

    public RestAddressService(){
        retrofit = new retrofit2.Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        addressService = retrofit.create(AddressService.class);
    }

    public AddressService getService(){
        return addressService;
    }
}
