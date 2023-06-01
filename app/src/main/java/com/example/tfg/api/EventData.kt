package com.example.tfg.api

data class EventData(
    var `data`: Data
) {
    data class Data(
        var attributes: Attributes,
        var id: Int
    ) {
        data class Attributes(
            var fecha_evento: String,
            var foto_evento: String,
            var hora_fin: String,
            var hora_inicio: String,
            var isFinished: Boolean,
            var nivel: String,
            var sexo: String,
            var titulo_evento: String,
            var updatedAt: String,
            var users: Users
        ) {
            data class Users(
                var `data`: List<Data>
            ) {
                data class Data(
                    var attributes: Attributes,
                    var id: Int
                ) {
                    data class Attributes(
                        var apellido: String,
                        var blocked: Boolean,
                        var confirmed: Boolean,
                        var createdAt: String,
                        var email: String,
                        var fecha_nacimiento: String,
                        var foto_perfil: String,
                        var foto_poster: String,
                        var mano_dominante: String,
                        var name: String,
                        var points: Int,
                        var provider: String,
                        var updatedAt: String,
                        var username: String
                    )
                }
            }
        }
    }
}