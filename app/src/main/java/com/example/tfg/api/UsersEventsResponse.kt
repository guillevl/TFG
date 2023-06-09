package com.example.tfg.api

data class UsersEventsResponse(
    val apellido: String,
    val blocked: Boolean,
    val confirmed: Boolean,
    val createdAt: String,
    val email: String,
    var events: MutableList<Event>,
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
) {
    data class Event(
        val fecha_evento: String,
        val foto_evento: String,
        val hora_fin: String,
        val hora_inicio: String,
        val id: Int,
        val isFinished: Boolean,
        val nivel: String,
        val sexo: String,
        val titulo_evento: String,
        val updatedAt: String
    )
}