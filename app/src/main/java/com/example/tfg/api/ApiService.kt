package com.example.tfg.api

import android.service.autofill.UserData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ApiService {
    //Registrar Usuario
    @POST("auth/local/register")
    fun meterUser(
        @Body request: RegisterData
    ): Call<RegisterResponse>

    //Logear Usuario
    @POST("auth/local")
    fun login(
        @Body credentials: LoginCredentials
    ): Call<LoginResponse>

    //Sacar usuario
    @GET("/users/{id}")
    fun getUserById(@Path("id") id: String): Call<UserData>
}