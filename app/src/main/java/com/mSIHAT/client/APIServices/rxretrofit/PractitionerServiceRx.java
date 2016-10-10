package com.mSIHAT.client.APIServices.rxretrofit;

import com.mSIHAT.client.models.Expertise;
import com.mSIHAT.client.models.Practitioner;
import com.mSIHAT.client.models.Pratitioner2;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by alamchristian on 3/29/16.
 */
public interface PractitionerServiceRx {
    @GET("practitioners/{practitioner_id}")
    Observable<Pratitioner2> getPractitionerById(@Path("practitioner_id") int practitioner_id);

    @GET("subservices/{subservice_id}/city/{city_id}/datetime/{search_date}/practitioners")
    Observable<List<Practitioner>> getAvailablePractitioners(@Path("subservice_id") int subservice_id,
                                                             @Path("city_id") int city_id,
                                                             @Path("search_date") String datetime);

    @GET("subservices/{subservice_id}/city/{city_id}/datetime/{search_date}/practitioners")
    Observable<Response<List<Practitioner>>> getAvailablePractitionersResponse(@Path("subservice_id") int subservice_id,
                                                                               @Path("city_id") int city_id,
                                                                               @Path("search_date") String datetime);

    @GET("practitioners/{practitioner_id}/subservices/{subservice_id}/expertise")
    Observable<Expertise> getPractitionerExpertise(@Path("practitioner_id") int practitioner_id,
                                                   @Path("subservice_id") int subservice_id);

    @GET("subservices/{subservice_id}/city/{city_id}/datetime/{start_date}/{frequency}/{amount}/practitioners")
    Observable<Response<List<Practitioner>>> getAvailablePractitionersForMultiResponse(@Path("subservice_id") int subservice_id,
                                                                                       @Path("city_id") int city_id,
                                                                                       @Path("start_date") String datetime,
                                                                                       @Path("frequency") String frequency,
                                                                                       @Path("amount") int amount);

    @GET("subservices/{subservice_id}/city/{city_id}/datetime/{start_date}/{frequency}/{amount}/practitioners")
    Observable<List<Practitioner>> getAvailablePractitionersForMulti(@Path("subservice_id") int subservice_id,
                                                                     @Path("city_id") int city_id,
                                                                     @Path("start_date") String datetime,
                                                                     @Path("frequency") String frequency,
                                                                     @Path("amount") int amount);

}
