package mx.edu.itesca.fridapawls_2026.data.model

data class Chat(
    val nombre: String,
    val ultimoMensaje: String,
    val hora: String,
    val noLeido: Boolean = false
)