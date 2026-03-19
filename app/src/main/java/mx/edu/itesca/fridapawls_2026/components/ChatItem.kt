package mx.edu.itesca.fridapawls_2026.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import mx.edu.itesca.fridapawls_2026.data.model.Chat

@Composable
fun ChatItem(chat: Chat, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 👤 Foto (placeholder)
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFF9B7BFF), CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(chat.nombre, fontWeight = FontWeight.Bold)
            Text(
                chat.ultimoMensaje,
                color = Color.Gray,
                maxLines = 1
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(chat.hora, fontSize = MaterialTheme.typography.labelSmall.fontSize)

            if (chat.noLeido) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Red, CircleShape)
                )
            }
        }
    }
}