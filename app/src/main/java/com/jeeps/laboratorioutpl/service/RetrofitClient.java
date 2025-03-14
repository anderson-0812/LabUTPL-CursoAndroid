package com.jeeps.laboratorioutpl.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

//    private static final String BASE_URL = "https://damp-hollows-96576.herokuapp.com";
//    private static final String BASE_URL = "http://172.16.14.219:3000"; // ip del suda

//    private static final String BASE_URL = "http://localhost:3000/";
    private static final String BASE_URL = "http://192.168.0.107:3000/"; // home
//    private static final String BASE_URL = "http://172.17.147.20:3000/"; // UTPL


    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
