package mx.edu.itesca.fridapawls_2026.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.itesca.fridapawls_2026.data.model.Chat
import mx.edu.itesca.fridapawls_2026.ui.components.ChatItem

@Composable
fun ChatsScreen() {

    val chats = remember {
        listOf(
            Chat("Juan", "¿Sigue disponible?", "10:30 AM", true),
            Chat("Ana", "Gracias por el apoyo 🙏", "Ayer", false),
            Chat("Carlos", "¿Dónde se entrega?", "Lun", true)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Text(
            text = "Chats",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn {
            items(chats) { chat ->
                ChatItem(chat) {
                    // 👉 aquí navegas a chat detalle
                }
            }
        }
    }
}