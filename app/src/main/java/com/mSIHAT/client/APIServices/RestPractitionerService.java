package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alamchristian on 3/22/16.
 */
public class RestPractitionerService {
    private retrofit2.Retrofit retrofit;
    private PractitionerService practitionerService;
    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(600, TimeUnit.SECONDS)
            .connectTimeout(600, TimeUnit.SECONDS)
            .build();

    public RestPractitionerService(){
        retrofit = new retrofit2.Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build();
        practitionerService = retrofit.create(PractitionerService.class);
    }

    public PractitionerService getService() { return practitionerService; }
}
