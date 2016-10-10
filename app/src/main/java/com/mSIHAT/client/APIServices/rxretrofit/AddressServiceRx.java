package com.mSIHAT.client.APIServices.rxretrofit;

import com.mSIHAT.client.models.address.Postcode;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by alamchristian on 3/31/16.
 */
public interface AddressServiceRx {
    @GET("postcodes/{postcode_id}")
    Observable<Postcode> getPostcodeById(@Path("postcode_id") int postcode_id);
}
