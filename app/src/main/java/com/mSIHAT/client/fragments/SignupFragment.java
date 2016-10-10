package com.mSIHAT.client.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestUserService;
import com.mSIHAT.client.MainActivity;
import com.mSIHAT.client.R;
import com.mSIHAT.client.models.UserP;
import com.mSIHAT.client.utils.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment extends Fragment implements View.OnClickListener {
    private ProgressDialog progressDialog;

    private Button btnSignup, btnCancel;
    private EditText editFullname, editPhone,
            editEmail, editPassword,
            editConfirmPassword, editCreditcard;

    private OnSignupAction mListener;

    private RestUserService restUserService;

    private UserP userP;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restUserService = new RestUserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View signupView = inflater.inflate(R.layout.fragment_signup, container, false);
        btnSignup = (Button) signupView.findViewById(R.id.btn_signup_confirm);
        btnSignup.setOnClickListener(this);
        btnCancel = (Button) signupView.findViewById(R.id.btn_signup_cancel);
        btnCancel.setOnClickListener(this);

        editFullname = (EditText) signupView.findViewById(R.id.edit_signup_fullname);
        editFullname.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editFullname, InputMethodManager.SHOW_FORCED);
        editPhone = (EditText) signupView.findViewById(R.id.edit_signup_phone);
        editEmail = (EditText) signupView.findViewById(R.id.edit_signup_email);
        editPassword = (EditText) signupView.findViewById(R.id.edit_signup_password);
        editConfirmPassword = (EditText) signupView.findViewById(R.id.edit_signup_confirm_password);


        return signupView;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {

        Log.e("not opening","gssfsdf");
            if (v.getId() == R.id.btn_signup_confirm) {
                if(checkEntries() == -1) {
                    progressDialog = new ProgressDialog(this.getContext());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage(getString(R.string.signing_you_up));
                    progressDialog.show();
                    if (editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
                        userP = new UserP(editFullname.getText().toString(), editEmail.getText().toString(),
                                editPassword.getText().toString(), editPhone.getText().toString(), "None");

                        if (v.getId() == R.id.btn_signup_confirm) {
                            Call<UserP> call = restUserService.getService().createUser(userP);
                            call.enqueue(new Callback<UserP>() {
                                @Override
                                public void onResponse(Call<UserP> call, Response<UserP> response) {
                                    if (response.code() == 201) {
                                        progressDialog.dismiss();
                                        login(response.body().id);
                                    }else
                                    {
                                        Toast.makeText(SignupFragment.this.getContext(), response.message(), Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();

                                    }
                                }

                                @Override
                                public void onFailure(Call<UserP> call, Throwable t) {
                                    Toast.makeText(SignupFragment.this.getContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    t.printStackTrace();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(this.getContext(), R.string.password_does_not_match, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                } else if(checkEntries() == 3){
                    Toast.makeText(SignupFragment.this.getContext(),
                            R.string.password_needs_to_be_at_least_8_chars_long, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(),
                            String.valueOf(checkEntries()), Toast.LENGTH_SHORT).show();

                }
            } else if (v.getId() == R.id.btn_signup_cancel) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.move, R.anim.moveout).hide(getActivity().getSupportFragmentManager().findFragmentByTag("signup")).
                        show(getActivity().getSupportFragmentManager().findFragmentByTag("welcomepage")).commit();
            }
        }


    private int checkEntries(){
        int errorAt = -1;
        String[] entries = new String[4];
        entries[0] = editFullname.getText().toString();
        entries[1] = editEmail.getText().toString();
        entries[2] = editPhone.getText().toString();
        entries[3] = editPassword.getText().toString();

        for(int i = 0 ; i < entries.length ; i++){
            if(entries[i] == null || entries[i].length() < 1){
                errorAt = i;
                break;
            }
            if(i == 1){
                if(!isValidEmail(entries[i])){
                    errorAt = i;
                    break;
                }
            }
            if(i == 3){
                if(entries[i].length() < 8 || entries[i].trim().length() < 8){
                    errorAt = i;
                    break;
                }
            }

        }
        return errorAt;
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

    private void login(int id){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.logging_in));
        progressDialog.show();
        Call<UserP> call = restUserService.getService().getUserById(id);
        call.enqueue(new Callback<UserP>() {
            @Override
            public void onResponse(Call<UserP> call, Response<UserP> response) {
                int statusCode = response.code();
                UserP userP = response.body();
                String msg = null;
                if (statusCode == 200) {
                    msg = getString(R.string.welcome) + userP.fullname + "!";
                    Intent loginIntent = new Intent(SignupFragment.this.getContext(), MainActivity.class);
                    Bundle loginBundle = new Bundle();
                    loginBundle.putInt(Constants.EXTRA_USER_ID, userP.id);
                    loginBundle.putString(Constants.EXTRA_USER_FULLNAME, userP.fullname);
                    loginBundle.putString(Constants.EXTRA_USER_EMAIL, userP.email);
                    loginIntent.putExtras(loginBundle);
                    startActivity(loginIntent);
                }
                progressDialog.dismiss();
                Toast.makeText(SignupFragment.this.getContext(), msg, Toast.LENGTH_SHORT).show();
//                mListener.onSignupButtonClick(true);
            }

            @Override
            public void onFailure(Call<UserP> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SignupFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnSignupAction {
        void onSignupButtonClick(boolean confirm);
    }
}
