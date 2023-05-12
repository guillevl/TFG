package com.example.tfg.api

class UserListResponse : ArrayList<UserListResponse.UserListResponseItem>(){
    data class UserListResponseItem(
        val apellido: String,
        val blocked: Boolean,
        val confirmed: Boolean,
        val createdAt: String,
        val email: String,
        val fecha_nacimiento: String,
        val foto_perfil: String,
        val foto_poster: String,
        val id: Int,
        val mano_dominante: String,
        val name: String,
        val points: Int,
        val provider: String,
        val updatedAt: String,
        val username: String
    )
}