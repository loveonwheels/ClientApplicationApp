package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.utils.Constants;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alamchristian on 3/3/16.
 */
public class RestPatientService {

    private retrofit2.Retrofit retrofit;
    private PatientService patientService;

    public RestPatientService() {
        retrofit = new retrofit2.Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        patientService = retrofit.create(PatientService.class);
    }

    public PatientService getService() {
        return patientService;
    }
}
