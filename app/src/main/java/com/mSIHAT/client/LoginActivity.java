package com.mSIHAT.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mSIHAT.client.APIServices.RestUserService;
import com.mSIHAT.client.MainActivity;import com.mSIHAT.client.R;import com.mSIHAT.client.utils.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSignup, btnLogin;
    private EditText editUsername, editPassword;
    private ProgressDialog progressBar;

    private RestUserService restUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editUsername = (EditText) findViewById(R.id.edit_login_username);
        editPassword = (EditText) findViewById(R.id.edit_login_password);

        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(this);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        restUserService = new RestUserService();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_login){

            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
            Bundle loginBundle = new Bundle();
            loginBundle.putInt(Constants.EXTRA_USER_ID, 1);
            loginBundle.putString(Constants.EXTRA_USER_FULLNAME, "dfdfdf");
            loginBundle.putString(Constants.EXTRA_USER_EMAIL, "Precious@yahoo.com");
            loginIntent.putExtras(loginBundle);
            startActivity(loginIntent);


            /*
            String username = editUsername.getText().toString();
            String password = editPassword.getText().toString();
            if(isValidEmail(username) && isValidPassword(password)){
                progressBar = new ProgressDialog(this);
                progressBar.setMessage(getString(R.string.logging_in));
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.setIndeterminate(true);
                progressBar.show();

                Call<UserP> call = restUserService.getService().validateLogin(username, password);
                call.enqueue(new Callback<UserP>() {
                    @Override
                    public void onResponse(Call<UserP> call, Response<UserP> response) {
                        int statusCode = response.code();
                        UserP userP = response.body();
                        String msg = null;
                        if (statusCode == 200) {
                            msg = getString(R.string.welcome) + userP.fullname + "!";
                            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle loginBundle = new Bundle();
                            loginBundle.putInt(Constants.EXTRA_USER_ID, userP.id);
                            loginBundle.putString(Constants.EXTRA_USER_FULLNAME, userP.fullname);
                            loginBundle.putString(Constants.EXTRA_USER_EMAIL, userP.email);
                            loginIntent.putExtras(loginBundle);
                            startActivity(loginIntent);
                        } else if (statusCode == 404)
                            msg = getString(R.string.invalid_username_and_or_password);
                        else
                            msg = String.valueOf(statusCode);
                        progressBar.dismiss();
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<UserP> call, Throwable t) {
                        progressBar.dismiss();
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                if(!isValidEmail(username)){
                    editUsername.setError(getString(R.string.invalid_username));
                }
                if(!isValidPassword(password)){
                    editPassword.setError(getString(R.string.invalid_password));
                }
            }
  */
        } else if(v.getId() == R.id.btn_signup){
            Intent signupIntent = new Intent(this, SignupActivity.class);
            startActivity(signupIntent);
        }
    }

    private boolean isValidEmail(String email){
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        if(email.trim().length() == 0){
            return false;
        } else {
            return matcher.matches();
        }
    }

    private boolean isValidPassword(String password){
        if(password != null && password.length() > 0){
            return true;
        }
        return false;
    }
}
