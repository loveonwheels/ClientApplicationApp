package com.mSIHAT.client.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestUserService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.dialogs.UpdateCreditcardDialog;
import com.mSIHAT.client.fragments.dialogs.UpdatePasswordDialog;
import com.mSIHAT.client.models.UserP;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alamchristian on 3/11/16.
 */

public class MyAccountUpdateFragment extends Fragment implements View.OnClickListener {
    public static final int UPDATE_PASSWORD_DIALOG = 11;
    public static final int UPDATE_CREDITCARD_DIALOG = 12;
    private ProgressDialog progressDialog;

    private UserP userP;

    private EditText editFullname, editEmail, editPhone, editCreditcard;
    private Button btnChangePassword, btnChangeCard;

    private static final String ARG_USER_OBJECT = "myaccountupdate_user_object";

    private FragmentActivity callingActivity;

    private OnMyAccountUpdateFragmentListener mListener;

    private RestUserService restUserService;

    public MyAccountUpdateFragment(){

    }

    public static MyAccountUpdateFragment newInstance (UserP userP){
        MyAccountUpdateFragment fragment = new MyAccountUpdateFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER_OBJECT, userP);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            userP = getArguments().getParcelable(ARG_USER_OBJECT);
        }
        restUserService = new RestUserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_account_update, container, false);
        getActivity().setTitle(getString(R.string.title_update_account));
        editFullname = (EditText) rootView.findViewById(R.id.edit_account_update_fullname);
        editFullname.setKeyListener(null);
        editEmail = (EditText) rootView.findViewById(R.id.edit_account_update_email);
        editEmail.setKeyListener(null);
        editCreditcard = (EditText) rootView.findViewById(R.id.edit_account_update_creditcard);
        editCreditcard.setKeyListener(null);
        editPhone = (EditText) rootView.findViewById(R.id.edit_account_update_phone);

        btnChangePassword = (Button) rootView.findViewById(R.id.button_account_update_change_password);
        btnChangePassword.setOnClickListener(this);
        btnChangeCard = (Button) rootView.findViewById(R.id.button_account_update_change_creditcard);
        btnChangeCard.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editFullname.setText(userP.fullname);
        editEmail.setText(userP.email);
        editCreditcard.setText(userP.creditcard);
        editPhone.setText(userP.phone);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_account_update, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_account_update_done:
                saveChanges(callingActivity.getString(R.string.updating_account), 1);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnMyAccountUpdateFragmentListener){
            mListener = (OnMyAccountUpdateFragmentListener) context;
            callingActivity = (FragmentActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMyAccountUpdateFragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_account_update_change_password:
                UpdatePasswordDialog updatePasswordDialog = UpdatePasswordDialog.newInstance(userP.password);
                updatePasswordDialog.setTargetFragment(MyAccountUpdateFragment.this, UPDATE_PASSWORD_DIALOG);
                updatePasswordDialog.show(callingActivity.getSupportFragmentManager(), "updatePasswordDialog");
                break;
            case R.id.button_account_update_change_creditcard:
                UpdateCreditcardDialog updateCreditcardDialog = UpdateCreditcardDialog.newInstance();
                updateCreditcardDialog.setTargetFragment(MyAccountUpdateFragment.this, UPDATE_CREDITCARD_DIALOG);
                updateCreditcardDialog.show(callingActivity.getSupportFragmentManager(), "updateCreditcardDialog");
                break;
            default:
                break;
        }
    }

    private boolean saveChanges(String msg, final int updateId){
        userP.phone = editPhone.getText().toString();
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(msg);
        progressDialog.show();
        Call<Void> call = restUserService.getService().updateUser(userP.id, userP);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 202) {
                    if(updateId == 1) {
                        Toast.makeText(MyAccountUpdateFragment.this.getContext(),
                                R.string.your_account_is_successfully_updated, Toast.LENGTH_SHORT).show();
                        mListener.onButtonActionBarButtonClick(true);
                    } else if(updateId == 3){
                        Toast.makeText(MyAccountUpdateFragment.this.getContext(),
                                R.string.successfully_updated_creditcard, Toast.LENGTH_SHORT).show();
                        editCreditcard.setText(userP.creditcard);
                    } else if(updateId == 2){
                        Toast.makeText(MyAccountUpdateFragment.this.getContext(),
                                R.string.successfully_updated_password, Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyAccountUpdateFragment.this.getContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case UPDATE_PASSWORD_DIALOG:
                if(resultCode == Activity.RESULT_OK){
                    userP.password = data.getStringExtra(UpdatePasswordDialog.NEW_PASSWORD);
                    saveChanges(callingActivity.getString(R.string.updating_password), 2);
                }
                break;
            case UPDATE_CREDITCARD_DIALOG:
                if(resultCode == Activity.RESULT_OK){
                    userP.creditcard = data.getStringExtra(UpdateCreditcardDialog.NEW_CREDITCARD);
                    saveChanges(callingActivity.getString(R.string.updating_creditcard), 3);
                }
            default:
                break;
        }
    }

    public interface OnMyAccountUpdateFragmentListener {
        void onButtonActionBarButtonClick(boolean confirm);
    }
}
