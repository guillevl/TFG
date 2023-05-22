package com.example.tfg.api

data class EventData(
        var fecha_evento: String,
        var isFinished: Boolean,
        var foto_evento: String,
        var hora_fin: String,
        var hora_inicio: String,
        var nivel: String,
        var sexo: String,
        var titulo_evento: String
)