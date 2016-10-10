package com.mSIHAT.client.APIServices.rxretrofit;

import com.mSIHAT.client.models.Subservice;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by alamchristian on 3/23/16.
 */
public interface ServicesAPIServiceRx {
    @GET("subservices/{subservice_id}")
    Observable<Subservice> getSubserviceById(@Path("subservice_id") int subservice_id);
}
