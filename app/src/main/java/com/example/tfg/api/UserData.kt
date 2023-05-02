package com.example.tfg.api

class UserData : ArrayList<UserData.UserDataItem>(){
    data class UserDataItem(
        val apellido: String,
        val blocked: Boolean,
        val confirmed: Boolean,
        val createdAt: String,
        val email: String,
        val fecha_nacimiento: String,
        val foto_perfil: Any,
        val foto_poster: Any,
        val id: Int,
        val mano_dominante: String,
        val name: String,
        val points: Int,
        val provider: String,
        val updatedAt: String,
        val username: String
    )
}