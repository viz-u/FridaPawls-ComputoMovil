package mx.edu.itesca.fridapawls_2026.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.itesca.fridapawls_2026.R
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost
import mx.edu.itesca.fridapawls_2026.ui.components.PostCard
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

// -------------------- FIRESTORE REPOSITORY --------------------

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getPostsPaginated(
        lastDoc: DocumentSnapshot?,
        limit: Long = 10,
        onResult: (List<MascotaPost>, DocumentSnapshot?) -> Unit
    ) {

        var query = db.collection("posts")
            .orderBy("timestamp") // 🔥 mejor que nombre
            .limit(limit)

        if (lastDoc != null) {
            query = query.startAfter(lastDoc)
        }

        query.get()
            .addOnSuccessListener { result ->

                val posts = result.documents.map { doc ->

                    MascotaPost(
                        id = doc.id,
                        nombreMascota = doc.getString("nombreMascota") ?: "",
                        descripcion = doc.getString("descripcion") ?: "",
                        ubicacion = doc.getString("ubicacion") ?: "",
                        estado = doc.getString("estado") ?: "",
                        imagenes = doc.get("imagenes") as? List<String> ?: emptyList(),
                        uid = doc.getString("uid") ?: "",
                        timestamp = doc.getTimestamp("timestamp")
                    )
                }

                onResult(posts, result.documents.lastOrNull())
            }
    }
}

// -------------------- HOME SCREEN --------------------

@Composable
fun HomeScreen(navController: NavController) {

    val repo = remember { FirestoreRepository() }

    var posts by remember { mutableStateOf(listOf<MascotaPost>()) }
    var lastDoc by remember { mutableStateOf<DocumentSnapshot?>(null) }

    var searchText by remember { mutableStateOf("") }

    // 🔥 CARGA INICIAL
    LaunchedEffect(Unit) {
        repo.getPostsPaginated(null) { list, last ->
            posts = list
            lastDoc = last
        }
    }

    // 🔥 CARGAR MÁS
    fun loadMore() {
        repo.getPostsPaginated(lastDoc) { list, last ->
            posts = posts + list
            lastDoc = last
        }
    }

    // 🔍 FILTRO
    val filteredPosts = remember(posts, searchText) {
        posts.filter {
            it.nombreMascota.contains(searchText, true) ||
                    it.descripcion.contains(searchText, true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        // 🔥 HEADER
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = null,
                tint = MainPurple,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "FridaPawls",
                style = MaterialTheme.typography.titleMedium,
                color = MainPurple
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔍 SEARCH BAR
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    RoundedCornerShape(50)
                )
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Buscar mascotas...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 📦 FEED
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {

            items(filteredPosts) { post ->

                PostCard(
                    post = post,
                    onChatClick = { /* chat */ },
                    onEditClick = { postId ->
                        if (postId.isNotBlank()) {
                            navController.navigate("edit_post/$postId")
                        }
                    }
                )
            }

            // 🔥 PAGINACIÓN
            item {
                LaunchedEffect(posts.size) {
                    if (posts.isNotEmpty()) {
                        loadMore()
                    }
                }
            }
        }
    }
}