package com.mSIHAT.client.APIServices;

/**
 * Created by alamchristian on 3/3/16.
 */
import com.mSIHAT.client.models.Patient;
import com.mSIHAT.client.models.views.PatientSelectionItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PatientService {
    @GET("Users/{user_id}/Patients")
    Call<List<Patient>> getPatientsOfUserId(@Path("user_id") int userId);
    @GET("Users/{user_id}/Patients")
    Call<ArrayList<PatientSelectionItem>> getPatientsOfUserIdArrayList(@Path("user_id") int userId);
    @GET("Patients/{patient_id}")
    Call<Patient> getPatientOfId(@Path("patient_id") int patientId);
    @POST("Patients/Register")
    Call<Patient> registerPatient(@Body Patient patient);
    @DELETE("Patients/Delete/{patient_id}")
    Call<Patient> deletePatient(@Path("patient_id") int patientId);
}
