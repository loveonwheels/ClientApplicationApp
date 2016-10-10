package com.mSIHAT.client.fragments.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestForServices;
import com.mSIHAT.client.R;
import com.mSIHAT.client.listAdapters.ServiceRatesListAdapter;
import com.mSIHAT.client.models.ServiceRate;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alamchristian on 4/15/16.
 */
public class ServiceRatesDialog extends DialogFragment {
    private static final String ARG_SERVICE_ID = "service_id";

    private List<ServiceRate> rates;
    private ListView rates_list;

    private int service_id;

    private RestForServices restForServices;

    public ServiceRatesDialog(){

    }

    public static ServiceRatesDialog newInstance(int service_id){
        ServiceRatesDialog dialog = new ServiceRatesDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_SERVICE_ID, service_id);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        service_id = getArguments().getInt(ARG_SERVICE_ID);
        restForServices = new RestForServices();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_service_rates, null);
        rates_list = (ListView) dialogView.findViewById(R.id.list_service_rates);
        getRates();
        builder.setView(dialogView);

        return builder.create();
    }

    private void getRates(){
        Call<List<ServiceRate>> callRates = restForServices.getServicesAPI().getRatesOfService(service_id);
        callRates.enqueue(new Callback<List<ServiceRate>>() {
            @Override
            public void onResponse(Call<List<ServiceRate>> call, Response<List<ServiceRate>> response) {
                if(response.code() == 200){
                    rates = response.body();
                    rates_list.setAdapter(new ServiceRatesListAdapter(getContext(), rates));
                }
            }

            @Override
            public void onFailure(Call<List<ServiceRate>> call, Throwable t) {
                Toast.makeText(ServiceRatesDialog.this.getContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                ServiceRatesDialog.this.dismiss();
            }
        });
    }
}
