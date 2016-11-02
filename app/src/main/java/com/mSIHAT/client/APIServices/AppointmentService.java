package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.models.Appointment;
import com.mSIHAT.client.models.Appointment2;
import com.mSIHAT.client.models.ConditionPost;
import com.mSIHAT.client.models.Feedback;
import com.mSIHAT.client.models.MultiAppointment;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by alamchristian on 3/23/16.
 */
public interface AppointmentService {
    @POST("appointments")
    Call<String> postAppointment(@Body Appointment2 appointment);
    @POST("appointments/conditions/add")
    Call<ConditionPost> postAppointmentCondition(@Body ConditionPost condition);

    @POST("appointments/create/multi/frequency/{frequency}/amount/{amount}")
    Call<MultiAppointment> postMultiAppointment(@Path("frequency") String frequency, @Path("amount") int amount,
                                                @Body Appointment2 appointment);
    @POST("multiappointments/{multi_appointment_id}/conditions/add")
    Call<ConditionPost> postMultiAppointmentCondition(@Path("multi_appointment_id") int multi_appointment_id,
                                                      @Body ConditionPost condition);

    @POST("appointments/feedback/submit")
    Call<Feedback> postAppointmentFeedback(@Body Feedback feedback);
    @GET("appointments/{appointment_id}/feedback")
    Call<Feedback> getFeedbackOfAppointment(@Path("appointment_id") int appointment_id);

    @DELETE("appointments/delete/{appointment_id}")
    Call<Appointment> deleteAppointmentById(@Path("appointment_id") int appointment_id);

    @DELETE("appointments/multi/delete/{multi_appointment_id}")
    Call<MultiAppointment> deleteMultiAppointmentById(@Path("multi_appointment_id") int multi_appointment_id);
}
