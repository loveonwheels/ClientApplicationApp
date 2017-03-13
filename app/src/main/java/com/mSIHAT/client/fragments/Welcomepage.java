package com.mSIHAT.client.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.braintreepayments.api.dropin.Customization;
import com.mSIHAT.client.APIServices.RestPractitionerService;
import com.mSIHAT.client.R;
import com.braintreepayments.*;
import com.mSIHAT.client.fragments.dialogs.RatingFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Welcomepage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Welcomepage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Welcomepage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE = 12343;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RestPractitionerService restPracService = new RestPractitionerService();
    private OnFragmentInteractionListener mListener;
    private ProgressDialog progressDialog;
    public Welcomepage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Welcomepage.
     */
    // TODO: Rename and change types and number of parameters
    public static Welcomepage newInstance(String param1, String param2) {
        Welcomepage fragment = new Welcomepage();
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
        View view = inflater.inflate(R.layout.fragment_welcomepage, container, false);
       Button takeatour = (Button)view.findViewById(R.id.btnTakeatour);

        takeatour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  FragmentManager fm = getActivity().getSupportFragmentManager();


                //getToken(1);
                String url = "http://www.msihat_.com/clienttakeatour";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });
        TextView signup = (TextView)view.findViewById(R.id.textView36);
        Button signBtn = (Button)view.findViewById(R.id.signBtn);
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.move, R.anim.moveout).hide(getActivity().getSupportFragmentManager().findFragmentByTag("welcomepage")).
                        add(R.id.firstpage, new LoginFragment(), "login").commit();




            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.move, R.anim.moveout).hide(getActivity().getSupportFragmentManager().findFragmentByTag("welcomepage")).
                        add(R.id.firstpage, new SignupFragment(), "signup").commit();

            }
        });
        return view;

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    public void onBraintreeSubmit(String token) {

        Customization customization = new Customization.CustomizationBuilder()
                .primaryDescription("Cart")
                .secondaryDescription("3 Items")
                .amount("$35")
                .submitButtonText("Confirm")
                .build();



        Intent intent = new Intent(getActivity(), BraintreePaymentActivity.class)
        .putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN,token).putExtra(BraintreePaymentActivity.EXTRA_CUSTOMIZATION, customization);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case BraintreePaymentActivity.RESULT_OK:
                    String paymentMethodNonce = data
                            .getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                    sendnounce(1,paymentMethodNonce);
                    Toast.makeText(getContext(),paymentMethodNonce,Toast.LENGTH_LONG).show();
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                    Toast.makeText(getContext(),"developer error",Toast.LENGTH_LONG).show();
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                    Toast.makeText(getContext(),"server error",Toast.LENGTH_LONG).show();
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    Toast.makeText(getContext(),"server not available at this time",Toast.LENGTH_LONG).show();
                    // handle errors here, a throwable may be available in
                    // data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void getToken(int userId){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        Log.e("prac id",String.valueOf(userId));
        Call<String> call = restPracService.getService().gettoken(userId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                int statusCode = response.code();


                if (statusCode == 200) {

String token = response.body();


                     progressDialog.dismiss();
                    onBraintreeSubmit(token);
                }else{
                    Toast.makeText(getActivity(),"failed",Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
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

    }


    private void sendnounce(int userId,String key){
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Confirming payment...");
        progressDialog.show();

        Log.e("prac id",String.valueOf(userId));
        Call<Boolean> call = restPracService.getService().sendnounce(userId,key);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                int statusCode = response.code();


                if (statusCode == 200) {

                    Boolean responseValue = response.body();

                    progressDialog.dismiss();
                    if(responseValue){

                        Toast.makeText(getActivity(),"Payment Sucessfull",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getActivity(),"Weare sorry but Payment Failed, Please try again",Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(getActivity(),"Payment Failed"+String.valueOf(statusCode),Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }


            }



            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                //   progress.dismiss();
                progressDialog.dismiss();
                Toast.makeText(getActivity(),"request failed here2" + t.getMessage(),Toast.LENGTH_LONG).show();

                //   Log.e("dfdf", t.toString());
            }
        });

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
