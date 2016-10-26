package com.mSIHAT.client.Notification;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireIDService extends FirebaseInstanceIdService {

    private RestPractitionerService practService = new RestPractitionerService();

    @Override
    public void onTokenRefresh() {
        String tkn = FirebaseInstanceId.getInstance().getToken();
        Log.d("Token oh23","Token ["+tkn+"]");




        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Constants.SHARED_DB, MODE_PRIVATE);
        int restoredUser = prefs.getInt("userid",0);

        if(restoredUser != 0){


            //update request
            updatetoken(restoredUser,tkn);
        }


        SharedPreferences.Editor editor = getSharedPreferences(Constants.SHARED_DB, MODE_PRIVATE).edit();
                editor.putString("fireToken", tkn);
                editor.commit();



 
    }

    public void updatetoken(int userid,String token){

        Call<Boolean> practSer = practService.getService().Updatetoken(userid,token);
        practSer.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 201){



                }
                else {

                    Log.e("completed token","completed");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }
}