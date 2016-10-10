package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.models.Service;
import com.mSIHAT.client.models.ServiceRate;
import com.mSIHAT.client.models.Subservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alamchristian on 3/22/16.
 */
public interface ServicesAPIService {
    @GET("services")
    Call<ArrayList<Service>> getAllServices();

    @GET("services/{service_id}/subservices")
    Call<ArrayList<Subservice>> getSubservicesOfService(@Path("service_id") int service_id);

    @GET("services/{service_id}/rates")
    Call<List<ServiceRate>> getRatesOfService(@Path("service_id") int service_id);
}
