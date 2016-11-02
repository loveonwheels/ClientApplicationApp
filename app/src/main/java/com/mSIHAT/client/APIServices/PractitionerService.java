package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.models.Practitioner;
import com.mSIHAT.client.models.PractitionerAvail;
import com.mSIHAT.client.models.PractitionerPartial;
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
                                                       @Query("search_time") int timeReq,
                                                       @Query("search_gender") int gender,
                                                       @Query("search_frq") int frq
                                                       );

    @PUT("FavoriteSearch/GetPractitioners")
    Call<List<Practitioner>> getFavAvailablePractitioners(@Query("subservice_id") int subservice_id,
                                                       @Query("city_id") int city_id,
                                                       @Query("search_date") String dateReq,
                                                       @Query("search_time") int timeReq,
                                                       @Query("search_gender") int gender,
                                                       @Query("search_frq") int frq,
                                                          @Query("user_id") int user_id
    );

    @PUT("Partialmatch/GetPractitioners")
    Call<List<PractitionerPartial>> getPartialAvailPractitioners(@Query("subservice_id") int subservice_id,
                                                                 @Query("city_id") int city_id,
                                                                 @Query("search_date") String dateReq,
                                                                 @Query("search_time") int timeReq,
                                                                 @Query("search_gender") int gender,
                                                                 @Query("search_frq") int frq
    );


    @POST("Notification/UpdateToken")
    Call<Boolean> Updatetoken(@Query("userid") int userid,
                                                                 @Query("token") String token
    );


    @POST("Favourite/MakeFavorite")
    Call<Boolean> makefavorite(@Query("practitionerid") int practitionerid,@Query("appointmentid") int appointmentid
    );

    @PUT("Favourite/GetPractitioners")
    Call<List<Practitioner>> getmyfavourite(@Query("user_id") int userid
    );

    @DELETE("Favourite/DeleteFavorite")
    Call<String> deletefavourite(@Query("practitioner_id") int practitioner_id,@Query("user_id") int userid
    );

    @GET("Hcp_Location/getImage")
    Call<String> getPracImage(@Query("userid") int userid
    );


}
