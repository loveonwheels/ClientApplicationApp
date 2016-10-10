package com.mSIHAT.client.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mSIHAT.client.APIServices.RestForServices;
import com.mSIHAT.client.R;
import com.mSIHAT.client.fragments.dialogs.ServiceRatesDialog;
import com.mSIHAT.client.listAdapters.ServicesListAdapter;
import com.mSIHAT.client.models.Service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alamchristian on 4/15/16.
 */
public class ServicesFragment extends Fragment implements ListView.OnItemClickListener {
    private ProgressDialog progressDialog;

    private RestForServices restForServices;

    private ArrayList<Service> services;
    private ListView services_list;

    public ServicesFragment(){

    }

    public static ServicesFragment newInstance(){
        return new ServicesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restForServices = new RestForServices();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_services, container, false);
        services_list = (ListView) rootView.findViewById(R.id.list_services);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        Call<ArrayList<Service>> callServices = restForServices.getServicesAPI().getAllServices();
        callServices.enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if(response.code() == 200){
                    services = response.body();
                    services_list.setAdapter(new ServicesListAdapter(getContext(), services));
                    services_list.setOnItemClickListener(ServicesFragment.this);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                Toast.makeText(ServicesFragment.this.getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ServiceRatesDialog dialog = ServiceRatesDialog.newInstance((int) id);
        dialog.show(getFragmentManager(), "ratesDialog");
    }
}
