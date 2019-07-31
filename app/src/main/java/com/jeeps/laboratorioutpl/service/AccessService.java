package com.jeeps.laboratorioutpl.service;

import com.jeeps.laboratorioutpl.model.access.AccessResult;
import com.jeeps.laboratorioutpl.model.access.RegisterAccess;
import com.jeeps.laboratorioutpl.model.access.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccessService {
    @GET("/acceso/{user}")
    Call<AccessResult> getAccessList(@Header("Token") String token, @Path("user") String userId);

    @POST("/acceso")
    Call<RegisterResult> postAccess(@Header("Token") String token, @Body RegisterAccess access);
}
