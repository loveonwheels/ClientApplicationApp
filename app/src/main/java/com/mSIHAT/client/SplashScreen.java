package com.mSIHAT.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mSIHAT.client.R;import com.mSIHAT.client.fragments.Welcomepage;

/**
 * Created by alamchristian on 3/8/16.
 */
public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        final AppCompatActivity act = this;
        act.getSupportFragmentManager().beginTransaction().addToBackStack("p1").
                setCustomAnimations(R.anim.move, R.anim.moveout).replace(R.id.firstpage, new Welcomepage(), "welcomepage").commit();
       /* new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);

        */
    }
}
