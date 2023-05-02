package com.example.tfg.api

data class RegisterData(
    val email: String,
    val password: String,
    val username: String,
    val name: String,
    val apellido: String,
    val fecha_nacimiento: String
)