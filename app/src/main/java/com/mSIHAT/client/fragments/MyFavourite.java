package com.mSIHAT.client.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.APIServices.RestUserService;
import com.mSIHAT.client.R;
import com.mSIHAT.client.UpdateDetailsActivity;
import com.mSIHAT.client.fragments.dialogs.Suggestpersonnel;
import com.mSIHAT.client.listAdapters.AvailablePractitionersListAdapter;
import com.mSIHAT.client.listAdapters.myFavPractitionersListAdapter;
import com.mSIHAT.client.models.Practitioner;
import com.mSIHAT.client.models.UserP;
import com.mSIHAT.client.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;

public class MyFavourite extends Fragment {
    private UserP userP;
    List<Practitioner> pracList;
    private RestPractitionerService restPracService = new RestPractitionerService();
    private ProgressDialog progressDialog;
    private EditText editFullname, editEmail, editPhone, editCreditcard;

    private static final String ARG_USER_ID = "myaccount_user_id";

    private int user_id;
    int item_position = 0;

    private RestUserService restUserService;

    private FragmentActivity callingActivity;
    ListView practitioners_list;
    public MyFavourite() {
        // Required empty public constructor
    }

    public static MyFavourite newInstance(int user_id) {
        MyFavourite fragment = new MyFavourite();
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
        View rootView = inflater.inflate(R.layout.fragment_myfavourite, container, false);

        practitioners_list = (ListView) rootView.findViewById(R.id.my_fav_prac_list);

        final AlertDialog.Builder builder_long = new AlertDialog.Builder(getActivity());
        final CharSequence renewal[] = getResources().getTextArray(R.array.reglongclick);
        builder_long.setItems(renewal, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setTitle("Confirm action");
                    builder.setMessage("Are you sure you want to delete this practitioner from your favourite ?");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog


                            deleteInfo(item_position);
                            dialog.dismiss();

                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }


        });

        practitioners_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                item_position = position;
                builder_long.show();
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(callingActivity.getString(R.string.retrieving_your_details));
        progressDialog.show();
        getPractitioners();



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


    private void getPractitioners(){


        Call<List<Practitioner>> call = restPracService.getService().getmyfavourite(user_id);

        call.enqueue(new Callback<List<Practitioner>>() {
            @Override
            public void onResponse(Call<List<Practitioner>> call, Response<List<Practitioner>> response) {

                Log.e("hereunkown","2324");
                int statusCode = response.code();
                pracList = response.body();
                String msg = "here";



                if (statusCode == 200) {



                    if(pracList.size() > 0){


                        practitioners_list.setAdapter(new myFavPractitionersListAdapter(getActivity().getBaseContext(), pracList));

                        //         practitioners_list.setOnItemClickListener(getActivity());


                        progressDialog.dismiss();
                    }else{
                        practitioners_list.setAdapter(new myFavPractitionersListAdapter(getActivity().getBaseContext(), pracList));
                        Log.e("no appointmnet","no appointment found");
                        progressDialog.dismiss();
                        /*
                        Log.e("here211","here211");
                        progressDialog.dismiss();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        Suggestpersonnel editNameDialog = new Suggestpersonnel();

                        editNameDialog.setTargetFragment(SelectPract.this, 1337);
                        editNameDialog.show(fm, "fragment_suggestpersonnel");
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
*/

                    }


                    //  progress.dismiss();
                }else{

                    practitioners_list.removeAllViewsInLayout();
                    progressDialog.dismiss();
                }
                Log.e("dfdf1", msg);

            }



            @Override
            public void onFailure(Call<List<Practitioner>> call, Throwable t) {
                //   progress.dismiss();
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"request failed here1",Toast.LENGTH_LONG).show();

                //   Log.e("dfdf", t.toString());
            }
        });
     /*   availablePractitioners
                .flatMapIterable(pracs -> pracs)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {

                    if(practitioners.size() > 0) {
                        Log.e("here17","here17");
                        practitioners_list.setAdapter(new AvailablePractitionersListAdapter(this.getActivity(), practitioners));
                        practitioners_list.setOnItemClickListener(this);
                    }else{

                        Log.e("here18","here18");
                    }
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }, throwable -> {
                    Log.e("here16","here16");
                    Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                           throwable.toString(), Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                    Log.e("here161", throwable.toString());
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                });
                */
    }

    public void deleteInfo(int pos){

        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Deleting");
        progressDialog.show();
        deletePractitioners(pracList.get(pos).hcpid);

    }


    private void deletePractitioners(int pratId){

Log.e("prac id",String.valueOf(pratId));
        Call<String> call = restPracService.getService().deletefavourite(pratId,user_id);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                int statusCode = response.code();


                if (statusCode == 200) {


getPractitioners();



                    //  progress.dismiss();
                }else{





                }


            }



            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //   progress.dismiss();
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"request failed here2" + t.getMessage(),Toast.LENGTH_LONG).show();

                //   Log.e("dfdf", t.toString());
            }
        });
     /*   availablePractitioners
                .flatMapIterable(pracs -> pracs)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {

                    if(practitioners.size() > 0) {
                        Log.e("here17","here17");
                        practitioners_list.setAdapter(new AvailablePractitionersListAdapter(this.getActivity(), practitioners));
                        practitioners_list.setOnItemClickListener(this);
                    }else{

                        Log.e("here18","here18");
                    }
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }, throwable -> {
                    Log.e("here16","here16");
                    Toast.makeText(AvailablePractitionersFragment.this.getContext(),
                           throwable.toString(), Toast.LENGTH_SHORT).show();
                    throwable.printStackTrace();
                    Log.e("here161", throwable.toString());
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                });
                */
    }
}
