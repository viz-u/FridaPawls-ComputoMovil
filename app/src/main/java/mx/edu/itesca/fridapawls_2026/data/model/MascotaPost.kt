package mx.edu.itesca.fridapawls_2026.data.model

data class MascotaPost(
    val nombre: String,
    var descripcion: String,
    val ubicacion: String,
    val estado: String,
    val imagenes: List<Int>,
    var liked: Boolean = false
)