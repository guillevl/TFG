package com.example.tfg.api

data class EventData(
    val `data`: Data
) {
    data class Data(
        val attributes: Attributes,
        val id: Int
    ) {
        data class Attributes(
            val createdAt: String,
            val fecha_evento: String,
            val foto_evento: String,
            val hora_fin: String,
            val hora_inicio: String,
            val isFinished: Boolean,
            val nivel: String,
            val sexo: String,
            val titulo_evento: String,
            val updatedAt: String,
            val users: Users
        ) {
            data class Users(
                val `data`: List<Data>
            ) {
                data class Data(
                    val attributes: Attributes,
                    val id: Int
                ) {
                    data class Attributes(
                        val apellido: String,
                        val blocked: Boolean,
                        val confirmed: Boolean,
                        val createdAt: String,
                        val email: String,
                        val fecha_nacimiento: String,
                        val foto_perfil: String,
                        val foto_poster: String,
                        val mano_dominante: String,
                        val name: String,
                        val points: Int,
                        val provider: String,
                        val updatedAt: String,
                        val username: String
                    )
                }
            }
        }
    }
}