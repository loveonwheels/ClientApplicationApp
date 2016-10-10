package com.mSIHAT.client.APIServices.rxretrofit;

import com.mSIHAT.client.models.Patient;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by alamchristian on 3/23/16.
 */
public interface PatientServiceRx {
    @GET("patients/{patient_id}")
    Observable<Patient> getPatientById(@Path("patient_id") int patient_id);
}
