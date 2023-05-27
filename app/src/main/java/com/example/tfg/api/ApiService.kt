package com.example.tfg.api

import retrofit2.Call
import retrofit2.http.*


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
    @GET("users/{id}")
    fun getUserById(@Path("id") id: String): Call<UserData>

    //Actualizar usuario
    @PUT("users/{id}")
    fun updateUser(
        @Body updatedUser: UserData,
        @Path("id") id: String
    ): Call<UserData>

    //Eliminar usuario
    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: String): Call<Unit>

    //Sacar Lista Usuarios
    @GET("users") //
    fun getUsers(): Call<UserListResponse>

    //Crear evento
    @POST("events?populate=*")
    fun crearEvento(
        @Body request: EventsResponse
    ): Call<EventsResponse>

    //Sacar los eventos que no estan terminados
    @GET("events?filters[isFinished]=false&populate=*")
    fun getEventsNotFinished(): Call<EventsNotFinishedResponse>

    //Sacar mis eventos
    @GET("events?populate=*")
    fun getMisEventos(
        @Query("filters[users][id]") id: String): Call<EventsNotFinishedResponse>
}