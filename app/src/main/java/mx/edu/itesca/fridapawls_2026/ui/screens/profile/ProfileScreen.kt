package mx.edu.itesca.fridapawls_2026.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost
import mx.edu.itesca.fridapawls_2026.ui.components.PostCard

@Composable
fun ProfileScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser

    var posts by remember { mutableStateOf(listOf<MascotaPost>()) }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    if (user == null) {
        LaunchedEffect(Unit) {
            navController.navigate("welcome") {
                popUpTo(0)
            }
        }
        return
    }

    // 🔥 CARGAR INFO USUARIO
    LaunchedEffect(Unit) {
        db.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                nombre = doc.getString("nombre") ?: "Usuario"
                email = doc.getString("email") ?: user.email ?: ""
            }

        // 🔥 POSTS DEL USUARIO
        db.collection("posts")
            .whereEqualTo("uid", user.uid)
            .get()
            .addOnSuccessListener { snapshot ->
                posts = snapshot.documents.mapNotNull {
                    it.toObject(MascotaPost::class.java)?.copy(id = it.id)
                }
            }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {

        item {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // 👤 ICONO (SIN IMAGEN)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = nombre.take(1).uppercase(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 👤 NOMBRE
                Text(
                    text = nombre,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                // 📧 EMAIL
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                    Button(
                        onClick = { navController.navigate("post") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Crear publicación")
                    }

                    OutlinedButton(
                        onClick = {
                            auth.signOut()
                            navController.navigate("welcome") {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Salir")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        items(posts) { post ->
            PostCard(
                post = post,
                onEditClick = {
                    navController.navigate("edit_post/$it")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}