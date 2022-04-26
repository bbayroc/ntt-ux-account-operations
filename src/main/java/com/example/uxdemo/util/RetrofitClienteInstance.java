package com.example.uxdemo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClienteInstance {

    private static Retrofit retrofit;
    private static Retrofit retrofit2;
    private static Retrofit retrofit3;
    private static Retrofit retrofit4;
    private static Retrofit retrofit5;
    private static String TRANSACTION = "Http://localhost:8040";
    private static String PERSONAL = "Http://localhost:8040";
    private static String ENTERPRISE = "Http://localhost:8040";
    private static String PRODUCT = "Http://localhost:8040";

    private static String CARD = "Http://localhost:8040";

    public static Retrofit getRetrofitTransaction() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(TRANSACTION)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitPersonal() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(PERSONAL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit2;
    }

    public static Retrofit getRetrofitEnterprise() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        if (retrofit3 == null) {
            retrofit3 = new Retrofit.Builder()
                    .baseUrl(ENTERPRISE)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit3;
    }

    public static Retrofit getRetrofitProduct() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        if (retrofit4 == null) {
            retrofit4 = new Retrofit.Builder()
                    .baseUrl(PRODUCT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit4;
    }

    public static Retrofit getRetrofitCard() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        if (retrofit5 == null) {
            retrofit5 = new Retrofit.Builder()
                    .baseUrl(CARD)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit5;
    }

}