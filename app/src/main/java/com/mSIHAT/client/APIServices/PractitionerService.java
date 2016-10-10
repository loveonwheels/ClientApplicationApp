package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.models.Practitioner;
import com.mSIHAT.client.models.PractitionerAvail;
import com.mSIHAT.client.models.ServiceRate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by alamchristian on 3/22/16.
 */
public interface PractitionerService {
    @GET("subservices/{subservice_id}/practitioners/{practitioner_id}/rate")
    Call<ServiceRate> getRateOfPractitionerForSubservice(@Path("subservice_id") int subservice_id,
                                                         @Path("practitioner_id") int practitioner_id);

    @DELETE("Practitioners/GetPractitionersForDate")
    Call<List<PractitionerAvail>> getAvailablePractitionersForDate(@Query("subservice_id") int subservice_id,
                                                                   @Query("city_id") int city_id,
                                                                   @Query("search_date") String dateReq
                                                                   );

    @PATCH("Practitioners/getPractitionersForTime")
    Call<List<PractitionerAvail>> getAvailablePractitionersForTime(@Query("subservice_id") int subservice_id,
                                                                   @Query("city_id") int city_id,
                                                                   @Query("search_time") int dateReq
    );


    @PUT("Practitioners/GetPractitioners")
    Call<List<Practitioner>> getAvailablePractitioners(@Query("subservice_id") int subservice_id,
                                                       @Query("city_id") int city_id,
                                                       @Query("search_date") String dateReq,
                                                       @Query("search_time") int timeReq
                                                       );


}
