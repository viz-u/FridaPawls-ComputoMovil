package mx.edu.itesca.fridapawls_2026.data.model

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val text: String = "",
    val timestamp: Long = 0,
    val type: String = "text"
)
