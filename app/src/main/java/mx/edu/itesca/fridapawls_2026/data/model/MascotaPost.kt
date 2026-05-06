package mx.edu.itesca.fridapawls_2026.data.model

import com.google.firebase.Timestamp

data class MascotaPost(
    val id: String = "",
    val nombreMascota: String = "",
    val descripcion: String = "",
    val ubicacion: String = "",
    val estado: String = "",
    val imagenes: List<String> = emptyList(),
    val uid: String = "",
    val timestamp: Timestamp? = null
)