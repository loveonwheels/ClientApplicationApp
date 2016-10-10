package com.mSIHAT.client.APIServices.rxretrofit;

import com.mSIHAT.client.models.Appointment;
import com.mSIHAT.client.models.AppointmentCondition;
import com.mSIHAT.client.models.MultiAppointment;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by alamchristian on 3/22/16.
 */
public interface AppointmentServiceRx {
    //responses
    @GET("users/{user_id}/appointments/confirmed")
    Observable<Response<List<Appointment>>> getConfirmedAppointmentsResponse(@Path("user_id") int user_id);
    @GET("users/{user_id}/appointments/pending")
    Observable<Response<List<Appointment>>> getPendingAppointmentsResponse(@Path("user_id") int user_id);
    @GET("users/{user_id}/appointments/completed")
    Observable<Response<List<Appointment>>> getCompletedAppointmentsResponse(@Path("user_id") int user_id);
    @GET("users/{user_id}/multiappointments/pending")
    Observable<Response<List<MultiAppointment>>> getPendingMultiAppointmentsResponse(@Path("user_id") int user_id);
    @GET("users/{user_id}/multiappointments/confirmed")
    Observable<Response<List<MultiAppointment>>> getConfirmedMultiAppointmentsResponse(@Path("user_id") int user_id);
    @GET("users/{user_id}/multiappointments/completed")
    Observable<Response<List<MultiAppointment>>> getCompletedMultiAppointmentsResponse(@Path("user_id") int user_id);
    //actual Objects
    @GET("appointments/{appointment_id}")
    Observable<Appointment> getAppointmentById(@Path("appointment_id") int appointment_id);

    @GET("users/{user_id}/appointments/confirmed")
    Observable<List<Appointment>> getConfirmedAppointments(@Path("user_id") int user_id);
    @GET("users/{user_id}/appointments/pending")
    Observable<List<Appointment>> getPendingAppointments(@Path("user_id") int user_id);
    @GET("users/{user_id}/appointments/completed")
    Observable<List<Appointment>> getCompletedAppointments(@Path("user_id") int user_id);

    @GET("appointments/{appointment_id}/conditions")
    Observable<AppointmentCondition> getConditionsOfAppointment(@Path("appointment_id") int appointment_id);

    @GET("users/{user_id}/multiappointments/pending")
    Observable<List<MultiAppointment>> getPendingMultiAppointments(@Path("user_id") int user_id);
    @GET("users/{user_id}/multiappointments/confirmed")
    Observable<List<MultiAppointment>> getConfirmedMultiAppointments(@Path("user_id") int user_id);
    @GET("users/{user_id}/multiappointments/completed")
    Observable<List<MultiAppointment>> getCompletedMultiAppointments(@Path("user_id") int user_id);

    @GET("multiappointments/{multi_appointment_id}/appointments")
    Observable<List<Appointment>> getAppointmentsOfMulti(@Path("multi_appointment_id") int multi_appointment_id);
}
