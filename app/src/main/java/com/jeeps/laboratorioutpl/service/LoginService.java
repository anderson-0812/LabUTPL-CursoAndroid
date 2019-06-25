package com.jeeps.laboratorioutpl.service;

import com.jeeps.laboratorioutpl.model.Login;
import com.jeeps.laboratorioutpl.model.LoginCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/login")
    Call<Login> login(@Body LoginCredentials credentials);
}
