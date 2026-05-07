package mx.edu.itesca.fridapawls_2026.ui.screens.chat

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.itesca.fridapawls_2026.data.model.Chat
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatsScreen(navController: NavController) {

    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val db = FirebaseFirestore.getInstance()

    val chats = remember { mutableStateListOf<Chat>() }
    val userNames = remember { mutableStateMapOf<String, String>() }

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(uid) {
        if (uid.isBlank()) return@LaunchedEffect

        db.collection("chats")
            .whereArrayContains("participants", uid)
            .addSnapshotListener { snap, error ->

                if (error != null) return@addSnapshotListener

                val list = snap?.documents?.mapNotNull {
                    it.toObject(Chat::class.java)?.copy(chatId = it.id)
                } ?: emptyList()

                chats.clear()
                chats.addAll(list.sortedByDescending { it.updatedAt })
            }
    }

    LaunchedEffect(chats.size) {
        chats.forEach { chat ->

            val otherUserId = chat.participants.firstOrNull { it != uid } ?: return@forEach

            if (!userNames.containsKey(otherUserId)) {
                db.collection("users")
                    .document(otherUserId)
                    .get()
                    .addOnSuccessListener { document ->

                        userNames[otherUserId] =
                            document.getString("nombre")
                                ?: document.getString("name")
                                        ?: document.getString("username")
                                        ?: "Usuario"
                    }
                    .addOnFailureListener {
                        userNames[otherUserId] = "Usuario"
                    }
            }
        }
    }

    val filteredChats = chats.filter { chat ->

        val otherUserId = chat.participants.firstOrNull { it != uid } ?: ""
        val userName = userNames[otherUserId] ?: "Usuario"

        searchText.isBlank() ||
                userName.contains(searchText, ignoreCase = true) ||
                chat.lastMessage.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        containerColor = Color(0xFFF7F7FB)
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Chats",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C1C1E)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Habla con otros usuarios sobre las mascotas",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7A7A7A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text("Buscar conversación")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF8E8E93)
                    )
                },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = MainPurple.copy(alpha = 0.40f),
                    unfocusedBorderColor = Color(0xFFE7E7EE)
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (filteredChats.isEmpty()) {

                EmptyChatsView()

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    items(filteredChats) { chat ->

                        val otherUserId = chat.participants.firstOrNull { it != uid } ?: ""
                        val otherUserName = userNames[otherUserId] ?: "Usuario"

                        ModernChatItem(
                            name = otherUserName,
                            lastMessage = chat.lastMessage.ifBlank { "Aún no hay mensajes" },
                            updatedAt = formatChatTime(chat.updatedAt),
                            onClick = {
                                if (otherUserId.isNotBlank() && chat.chatId.isNotBlank()) {

                                    val encodedChatId = Uri.encode(chat.chatId)
                                    val encodedOtherUser = Uri.encode(otherUserId)

                                    navController.navigate(
                                        "chat?chatId=$encodedChatId&receiverId=$encodedOtherUser"
                                    )
                                }
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(90.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ModernChatItem(
    name: String,
    lastMessage: String,
    updatedAt: String,
    onClick: () -> Unit
) {
    val initial = getInitialFromName(name)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(MainPurple.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MainPurple
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = name,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1C1C1E),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = updatedAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8E8E93)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF6E6E73),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                tint = MainBlue.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun EmptyChatsView() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 90.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(82.dp)
                .clip(CircleShape)
                .background(MainPurple.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MainPurple,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Aún no tienes chats",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1C1C1E)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Cuando inicies una conversación aparecerá aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF7A7A7A)
        )
    }
}

private fun getInitialFromName(name: String): String {
    val cleanName = name.trim()

    if (cleanName.isBlank()) return "U"

    return cleanName
        .first()
        .uppercase()
}

private fun formatChatTime(updatedAt: Long): String {
    if (updatedAt == 0L) return ""

    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(updatedAt))
}