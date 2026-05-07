package mx.edu.itesca.fridapawls_2026.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.itesca.fridapawls_2026.data.model.Chat

@Composable
fun ChatItem(
    chat: Chat,
    currentUserId: String,
    onClick: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()

    val receiverId = chat.participants.firstOrNull { it != currentUserId }

    var name by remember { mutableStateOf("Usuario") }

    LaunchedEffect(receiverId) {

        if (receiverId.isNullOrBlank()) return@LaunchedEffect

        db.collection("users")
            .document(receiverId)
            .get()
            .addOnSuccessListener {
                name = it.getString("nombre") ?: "Usuario"
            }
    }

    Card(
        onClick = onClick,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(name)
            Text(chat.lastMessage)
        }
    }
}