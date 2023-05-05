package com.example.tfg.api
data class UserData(
    var apellido: String,
    var blocked: Boolean,
    var confirmed: Boolean,
    var createdAt: String,
    var email: String,
    var fecha_nacimiento: String,
    var foto_perfil: String,
    var foto_poster: String,
    var id: Int,
    var mano_dominante: String,
    var name: String,
    var points: Int,
    var provider: String,
    var updatedAt: String,
    var username: String
)