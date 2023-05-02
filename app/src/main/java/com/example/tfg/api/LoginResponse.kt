package com.example.tfg.api

data class LoginResponse(
    val jwt: String,
    val user: User
) {
    data class User(
        val apellido: String,
        val blocked: Boolean,
        val confirmed: Boolean,
        val createdAt: String,
        val email: String,
        val fecha_nacimiento: String,
        val foto_perfil: Any,
        val foto_poster: Any,
        val id: Int,
        val mano_dominante: Any,
        val name: String,
        val points: Int,
        val provider: String,
        val updatedAt: String,
        val username: String
    )
}