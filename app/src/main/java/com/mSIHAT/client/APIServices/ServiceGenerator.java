package com.mSIHAT.client.APIServices;

import android.text.TextUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
//import com.wealdtech.hawk.HawkCredentials;
//import io.futurestud.retrofitbook.android.services.models.AccessToken;
//import okhttp3.logging.HttpLoggingInterceptor;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class ServiceGenerator {
    public static String apiBaseUrl = "https://maps.googleapis.com/";



    public static String getApiBaseUrl() {
        return apiBaseUrl;
    }

    public static void setApiBaseUrl(String apiBaseUrl) {
        ServiceGenerator.apiBaseUrl = apiBaseUrl;
    }



    private static Retrofit retrofit;


    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(ScalarsConverterFactory.create())
//                    .addConverterFactory(SimpleXmlConverterFactory.create())
                            //.addConverterFactory(new PolymorphicCustomConverter())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(apiBaseUrl);



    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, "");
    }


    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
           // httpClient.addInterceptor(
             //       new AuthenticationInterceptor(authToken));
        }

        return create(serviceClass);
    }

    private static <S> S create(Class<S> serviceClass) {
  /*      if (! httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
        }
*/
        builder.client(httpClient.build());
        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit() {
        return retrofit;
    }


}
