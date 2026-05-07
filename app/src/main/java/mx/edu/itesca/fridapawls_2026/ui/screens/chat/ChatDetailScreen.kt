package mx.edu.itesca.fridapawls_2026.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val text: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val timestamp: Timestamp? = null
)

@Composable
fun ChatDetailScreen(
    chatId: String,
    currentUserId: String,
    receiverId: String
) {
    val db = FirebaseFirestore.getInstance()
    val messages = remember { mutableStateListOf<Message>() }
    var input by remember { mutableStateOf("") }
    var receiverName by remember { mutableStateOf("Usuario") }
    val listState = rememberLazyListState()

    LaunchedEffect(receiverId) {
        if (receiverId.isBlank()) return@LaunchedEffect

        db.collection("users")
            .document(receiverId)
            .get()
            .addOnSuccessListener { document ->
                receiverName = document.getString("nombre")
                    ?: document.getString("name")
                            ?: document.getString("username")
                            ?: "Usuario"
            }
            .addOnFailureListener {
                receiverName = "Usuario"
            }
    }

    DisposableEffect(chatId) {
        var registration: ListenerRegistration? = null

        if (chatId.isNotBlank()) {
            registration = db.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener { snap, error ->

                    if (error != null) return@addSnapshotListener

                    val list = snap?.documents?.mapNotNull { document ->
                        try {
                            document.toObject(Message::class.java)
                        } catch (e: Exception) {
                            null
                        }
                    } ?: emptyList()

                    messages.clear()
                    messages.addAll(list)
                }
        }

        onDispose {
            registration?.remove()
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Scaffold(
        containerColor = Color(0xFFF7F7FB),
        topBar = {
            ModernChatHeader(receiverName = receiverName)
        },
        bottomBar = {
            ModernInputBar(
                input = input,
                onInputChange = { input = it },
                onSendClick = {
                    if (input.isBlank()) return@ModernInputBar

                    val messageText = input.trim()
                    val now = Timestamp.now()

                    val msg = hashMapOf(
                        "text" to messageText,
                        "senderId" to currentUserId,
                        "receiverId" to receiverId,
                        "timestamp" to now
                    )

                    db.collection("chats")
                        .document(chatId)
                        .collection("messages")
                        .add(msg)

                    db.collection("chats")
                        .document(chatId)
                        .set(
                            hashMapOf(
                                "chatId" to chatId,
                                "participants" to listOf(currentUserId, receiverId),
                                "lastMessage" to messageText,
                                "updatedAt" to System.currentTimeMillis()
                            ),
                            SetOptions.merge()
                        )

                    input = ""
                }
            )
        }
    ) { paddingValues ->

        if (messages.isEmpty()) {
            EmptyConversationView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                receiverName = receiverName
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                items(messages) { msg ->
                    val isMe = msg.senderId == currentUserId

                    ModernMessageBubble(
                        text = msg.text,
                        isMe = isMe,
                        time = formatMessageTime(msg.timestamp)
                    )
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
        }
    }
}

@Composable
private fun ModernChatHeader(receiverName: String) {
    Surface(
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MainPurple.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = receiverName.firstOrNull()?.uppercase() ?: "U",
                    fontWeight = FontWeight.Bold,
                    color = MainPurple
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = receiverName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1C1C1E)
                )

                Text(
                    text = "Conversación sobre mascota",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF8E8E93)
                )
            }

            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                tint = MainBlue
            )
        }
    }
}

@Composable
private fun ModernMessageBubble(
    text: String,
    isMe: Boolean,
    time: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .background(
                        color = if (isMe) MainBlue else Color.White,
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = if (isMe) 20.dp else 6.dp,
                            bottomEnd = if (isMe) 6.dp else 20.dp
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = text,
                    color = if (isMe) Color.White else Color(0xFF1C1C1E),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF8E8E93)
            )
        }
    }
}

@Composable
private fun ModernInputBar(
    input: String,
    onInputChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Escribe un mensaje...")
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF8F8FC),
                    unfocusedContainerColor = Color(0xFFF8F8FC),
                    focusedBorderColor = MainPurple.copy(alpha = 0.35f),
                    unfocusedBorderColor = Color(0xFFE7E7EE)
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MainPurple),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onSendClick) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyConversationView(
    modifier: Modifier = Modifier,
    receiverName: String
) {
    Column(
        modifier = modifier.padding(horizontal = 26.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(CircleShape)
                .background(MainPurple.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                tint = MainPurple,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Empieza la conversación",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1C1C1E)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Envía un mensaje a $receiverName para hablar sobre la mascota.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF7A7A7A),
            textAlign = TextAlign.Center
        )
    }
}

private fun formatMessageTime(timestamp: Timestamp?): String {
    if (timestamp == null) return ""

    val millis = timestamp.toDate().time
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(millis))
}