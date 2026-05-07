package mx.edu.itesca.fridapawls_2026.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.Timestamp

object ChatRepository {

    private val db = FirebaseFirestore.getInstance()

    // 🔥 CREAR CHAT SIEMPRE BIEN FORMADO
    fun createChatIfNotExists(
        chatId: String,
        user1: String,
        user2: String
    ) {
        val chatRef = db.collection("chats").document(chatId)

        chatRef.get().addOnSuccessListener { doc ->

            if (!doc.exists()) {

                chatRef.set(
                    hashMapOf(
                        "participants" to listOf(user1, user2),
                        "lastMessage" to "",
                        "lastMessageTime" to FieldValue.serverTimestamp()
                    )
                )
            } else {

                // 🔥 FIX AUTOMÁTICO si faltan IDs
                chatRef.update(
                    "participants",
                    listOf(user1, user2)
                )
            }
        }
    }

    // 💬 ENVIAR MENSAJE + ACTUALIZAR CHAT
    fun sendMessage(
        chatId: String,
        senderId: String,
        receiverId: String,
        text: String
    ) {

        val message = hashMapOf(
            "senderId" to senderId,
            "receiverId" to receiverId,
            "text" to text,
            "timestamp" to Timestamp.now()
        )

        // 1. mensaje
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)

        // 2. actualizar chat (ESTO HACE QUE APAREZCA INSTANTE)
        db.collection("chats")
            .document(chatId)
            .set(
                hashMapOf(
                    "participants" to listOf(senderId, receiverId),
                    "lastMessage" to text,
                    "lastMessageTime" to FieldValue.serverTimestamp()
                ),
                SetOptions.merge()
            )
    }
}