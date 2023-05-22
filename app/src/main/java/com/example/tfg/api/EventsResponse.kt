package com.example.tfg.api

data class EventsResponse(
    val data: Data.Attributes
) {
    data class Data(
        val attributes: String,
        val id: Int
    ) {
        data class Attributes(
            val fecha_evento: String,
            val foto_evento: String,
            val hora_fin: String,
            val hora_inicio: String,
            val isFinished: Boolean,
            val nivel: String,
            val sexo: String,
            val titulo_evento: String,
        )
    }
}