package com.mSIHAT.client.APIServices;

import com.mSIHAT.client.models.address.City;
import com.mSIHAT.client.models.address.Country;
import com.mSIHAT.client.models.address.Postcode;
import com.mSIHAT.client.models.address.State;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by alamchristian on 3/14/16.
 */
public interface AddressService {
    @GET("countries")
    Call<ArrayList<Country>> getAllCountries();

    @GET("countries/{country_id}/states")
    Call<ArrayList<State>> getStatesOfCountry(@Path("country_id") int country_id);

    @GET("states/{state_id}/cities")
    Call<ArrayList<City>> getCitiesOfState(@Path("state_id") int state_id);

    @GET("cities/{city_id}/postcodes")
    Call<ArrayList<Postcode>> getPostcodesOfCity(@Path("city_id") int city_id);
}
