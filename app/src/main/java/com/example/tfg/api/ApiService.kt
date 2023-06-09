package com.example.tfg.api

import org.json.JSONObject
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
    @GET("users?filters[username][\$ne]=admin") //
    fun getUsers(): Call<UserListResponse>

    //Crear evento
    @POST("events?populate=*")
    fun crearEvento(
        @Body request: EventsResponse
    ): Call<EventsResponse>

    //Sacar los eventos que no estan terminados
    @GET("events")
    fun getEventsNotFinished(
        @Query("filters[isFinished]") isFinished: Boolean,
        @Query("populate") populate: String,
        @Query("filters[\$or][0][titulo_evento][\$containsi]")tit:String,
        @Query("filters[\$or][1][fecha_evento][\$containsi]")fech:String,
        @Query("filters[\$or][2][sexo][\$containsi]")sex:String,
        @Query("filters[\$or][3][nivel][\$containsi]")nivl:String,
        @Query("filters[\$or][4][hora_inicio][\$containsi]")hora:String
    ): Call<EventsNotFinishedResponse>

    //Sacar mis eventos
    @GET("events")
    fun getMisEventos(
        @Query("filters[users][id]") id: String,
        @Query("populate") populate: String,
        @Query("filters[\$or][0][titulo_evento][\$containsi]")tit:String,
        @Query("filters[\$or][1][fecha_evento][\$containsi]")fech:String,
        @Query("filters[\$or][2][sexo][\$containsi]")sex:String,
        @Query("filters[\$or][3][nivel][\$containsi]")nivl:String,
        @Query("filters[\$or][4][hora_inicio][\$containsi]")hora:String
    ): Call<EventsNotFinishedResponse>

    //Sacar un evento
    @GET("events/{id}?populate=*")
    fun getEventById(@Path("id") id: String): Call<EventData>

    //actualizar evento
    @PUT("events/{id}")
    fun updateEvent(
        @Body updatedEvent: EventsResponse,
        @Path("id") id: String
    ): Call<EventData>

    //sacar el ranking de los ususarios
    @GET("users?sort[0]=points:desc&filters[username][\$not]=admin")
    fun getRankingUsers(): Call<UserListResponse>

    //añadir o quitar relacion evento-usuario
    @PUT("users/{id}")
    fun updateRelacion(
        @Body updateRelacion: UsersEventsResponse,
        @Path("id") id: String
    ): Call<UsersEventsResponse>
    //sacar usuario para hacer el put
    @GET("users/{id}?populate=events")
    fun getUsersEvents(@Path("id") id: String): Call<UsersEventsResponse>
}