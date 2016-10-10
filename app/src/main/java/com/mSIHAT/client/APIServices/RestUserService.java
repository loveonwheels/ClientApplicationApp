package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.utils.Constants;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alamchristian on 3/2/16.
 */
public class RestUserService {

    private retrofit2.Retrofit retrofit;
    private UserService userService;

    public RestUserService()
    {
        retrofit = new retrofit2.Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        userService = retrofit.create(UserService.class);
    }

    public UserService getService()
    {
        return userService;
    }
}
