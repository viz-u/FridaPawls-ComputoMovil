package mx.edu.itesca.fridapawls_2026.data.model

data class Chat(
    val chatId: String = "",
    val participants: List<String> = emptyList(),
    val lastMessage: String = "",
    val updatedAt: Long = 0L
)