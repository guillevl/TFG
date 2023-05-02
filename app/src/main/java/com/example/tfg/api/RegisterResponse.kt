package com.example.tfg.api

data class RegisterResponse(
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
        val role: Role,
        val updatedAt: String,
        val username: String
    ) {
        data class Role(
            val createdAt: String,
            val description: String,
            val id: Int,
            val name: String,
            val type: String,
            val updatedAt: String
        )
    }
}