package com.jeeps.laboratorioutpl.service;

import com.jeeps.laboratorioutpl.model.user.UserDB;
import com.jeeps.laboratorioutpl.model.user.UserEdit;
import com.jeeps.laboratorioutpl.model.user.UserPutResult;
import com.jeeps.laboratorioutpl.model.user.UserResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("/usuarios")
//    @GET("/users")
    Call<UserResult> getAllUsers(@Header("Token") String token);

    @GET("/usuarios/{user}")
//    @GET("/users/{user}")
    Call<UserDB> getUser(@Header("Token") String token, @Path("user") String userId);

    @PUT("/usuarios/{user}")
//    @PUT("/users/{user}")
    Call<UserPutResult> putUser(@Header("Token") String token, @Path("user") String userId, @Body UserEdit access);
}
