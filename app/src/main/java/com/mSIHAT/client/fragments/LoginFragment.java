package com.mSIHAT.client.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText username;
    EditText password;
    private ProgressDialog progressBar;

    private RestUserService restUserService;


    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        restUserService = new RestUserService();
        Button login = (Button)view.findViewById(R.id.btnLogin);
        username = (EditText) view.findViewById(R.id.input_username);
        password = (EditText) view.findViewById(R.id.input_pwd);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernametxt = username.getText().toString();
                String passwordtxt = password.getText().toString();
                if(isValidEmail(usernametxt) && isValidPassword(passwordtxt)){
                    progressBar = new ProgressDialog(getActivity());
                    progressBar.setMessage(getString(R.string.logging_in));
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setIndeterminate(true);
                    progressBar.show();

                    Call<UserP> call = restUserService.getService().validateLogin(usernametxt, passwordtxt);
                    call.enqueue(new Callback<UserP>() {
                        @Override
                        public void onResponse(Call<UserP> call, Response<UserP> response) {
                            int statusCode = response.code();
                            UserP userP = response.body();
                            String msg = null;
                            if (statusCode == 200) {
                                msg = getString(R.string.welcome) + userP.fullname + "!";
                                Intent loginIntent = new Intent(getActivity(), MainActivity.class);
                                Bundle loginBundle = new Bundle();
                                loginBundle.putInt(Constants.EXTRA_USER_ID, userP.id);
                                loginBundle.putString(Constants.EXTRA_USER_FULLNAME, userP.fullname);
                                loginBundle.putString(Constants.EXTRA_USER_EMAIL, userP.email);
                                loginIntent.putExtras(loginBundle);
                                startActivity(loginIntent);
                            } else if (statusCode == 404){
                                msg = getString(R.string.invalid_username_and_or_password);
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                                builder.setMessage("Authentication failed verify username and password")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });
                                android.app.AlertDialog alert = builder.create();
                                alert.show();
                            }


                            else
                                msg = String.valueOf(statusCode);
                            progressBar.dismiss();
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<UserP> call, Throwable t) {
                            progressBar.dismiss();
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    if(!isValidEmail(usernametxt)){
                       username.setError(getString(R.string.invalid_username));
                    }
                    if(!isValidPassword(passwordtxt)){
                      password.setError(getString(R.string.invalid_password));
                    }
                }


            }
        });
     /*   register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), RegistrationActivity.class);
                getActivity().startActivity(intent);

                getActivity().overridePendingTransition(R.anim.move, R.anim.moveout);

            }
        });*/

        return view;
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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
