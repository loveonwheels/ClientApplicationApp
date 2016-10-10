package com.mSIHAT.client.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestUserService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.UpdateDetailsActivity;
import com.mSIHAT.client.models.UserP;
import com.mSIHAT.client.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountFragment extends Fragment {
    private UserP userP;

    private ProgressDialog progressDialog;
    private EditText editFullname, editEmail, editPhone, editCreditcard;

    private static final String ARG_USER_ID = "myaccount_user_id";

    private int user_id;

    private RestUserService restUserService;

    private FragmentActivity callingActivity;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    public static MyAccountFragment newInstance(int user_id) {
        MyAccountFragment fragment = new MyAccountFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            user_id = getArguments().getInt(ARG_USER_ID);
        }
        restUserService = new RestUserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
        editFullname = (EditText) rootView.findViewById(R.id.edit_account_fullname);
        editFullname.setKeyListener(null);
        editEmail = (EditText) rootView.findViewById(R.id.edit_account_email);
        editEmail.setKeyListener(null);
        editPhone = (EditText) rootView.findViewById(R.id.edit_account_phone);
        editPhone.setKeyListener(null);
        editCreditcard = (EditText) rootView.findViewById(R.id.edit_account_creditcard);
        editCreditcard.setKeyListener(null);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(callingActivity.getString(R.string.retrieving_your_details));
        progressDialog.show();
        Call<UserP> call = restUserService.getService().getUserById(user_id);
        call.enqueue(new Callback<UserP>() {
            @Override
            public void onResponse(Call<UserP> call, Response<UserP> response) {
                if (response.code() == 200) {
                    userP = response.body();
                    editFullname.setText(userP.fullname);
                    editEmail.setText(userP.email);
                    editPhone.setText(userP.phone);
                    editCreditcard.setText(userP.creditcard);
                } else {
                    Toast.makeText(MyAccountFragment.this.getContext(),
                            String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserP> call, Throwable t) {
                Toast.makeText(MyAccountFragment.this.getContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_my_account_edit:
                Intent updateAccount = new Intent(this.getContext(), UpdateDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.PARCEL_USER_OBJECT, userP);
                bundle.putString(Constants.KEY_UPDATE_DETAILS_PURPOSE, Constants.EXTRA_UPDATE_ACCOUNT_DETAILS);
                updateAccount.putExtras(bundle);
                startActivityForResult(updateAccount, Constants.ACTIVITY_RESULT_ACCOUNT_UPDATE);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ACTIVITY_RESULT_ACCOUNT_UPDATE) {
                Fragment frag = callingActivity.getSupportFragmentManager().findFragmentByTag(Constants.MAIN_FRAGMENT_TAG);
                FragmentTransaction ft = callingActivity.getSupportFragmentManager().beginTransaction();
                ft.detach(frag).attach(frag).commit();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callingActivity = (FragmentActivity) context;


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
