package com.mSIHAT.client;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mSIHAT.client.R;import com.mSIHAT.client.fragments.SignupFragment;

public class SignupActivity extends AppCompatActivity implements SignupFragment.OnSignupAction {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SignupFragment signupFragment = SignupFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.signup_frag_container, signupFragment);
        transaction.commit();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onSignupButtonClick(boolean confirm) {
        if(confirm){
            this.finish();
        }else{
            this.finish();
        }
    }
}
