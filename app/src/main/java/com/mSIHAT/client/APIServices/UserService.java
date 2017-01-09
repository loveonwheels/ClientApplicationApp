package com.mSIHAT.client.APIServices;

/**
 * Created by alamchristian on 3/2/16.
 */
import com.mSIHAT.client.models.UserP;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("Users/{user_id}")
    Call<UserP> getUserById(@Path("user_id") int user_id);
    @GET("Users/Validate/{email}/{password}")
    Call<UserP> validateLogin(@Path("email") String email, @Path("password") String password);

    @GET("AppCom/ConfirmCompletedappointment")
    Call<Boolean> ConComsccepted (@Query("appointmentid") int appointmentid,@Query("rating") int rating,@Query("comment") String comment);

    @POST("Users/Signup")
    Call<UserP> createUser(@Body UserP user);

    @PUT("users/update/{user_id}")
    Call<Void> updateUser(@Path("user_id") int user_id, @Body UserP userP);
}