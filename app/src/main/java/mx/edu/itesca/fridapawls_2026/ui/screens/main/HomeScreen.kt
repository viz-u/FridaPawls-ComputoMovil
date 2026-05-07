package mx.edu.itesca.fridapawls_2026.ui.screens.main

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.itesca.fridapawls_2026.R
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost
import mx.edu.itesca.fridapawls_2026.data.repository.FirestoreRepository
import mx.edu.itesca.fridapawls_2026.ui.components.PostCard
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    val posts = remember { mutableStateListOf<MascotaPost>() }
    var lastDoc by remember { mutableStateOf<DocumentSnapshot?>(null) }

    var searchText by remember { mutableStateOf("") }
    var tipoFiltro by remember { mutableStateOf("Todos") }
    var ubicacionFiltro by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        FirestoreRepository.getPostsPaginated(null) { list, last ->
            posts.clear()
            posts.addAll(list)
            lastDoc = last
        }
    }

    fun loadMore() {
        if (lastDoc == null && posts.isNotEmpty()) return

        FirestoreRepository.getPostsPaginated(lastDoc) { list, last ->
            posts.addAll(list)
            lastDoc = last
        }
    }

    fun normalizeText(value: String): String {
        return value
            .trim()
            .lowercase()
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ü", "u")
    }

    fun getAnimalType(value: String): String {
        val text = normalizeText(value)

        return when {
            text.contains("perro") || text.contains("perros") || text.contains("canino") -> "perro"
            text.contains("gato") || text.contains("gatos") || text.contains("felino") -> "gato"
            else -> text
        }
    }

    val filteredPosts = posts.filter { post ->

        val search = normalizeText(searchText)
        val ubicacionSearch = normalizeText(ubicacionFiltro)

        val nombre = normalizeText(post.nombreMascota)
        val descripcion = normalizeText(post.descripcion)
        val ubicacion = normalizeText(post.ubicacion)
        val estado = normalizeText(post.estado)

        val tipoOriginal = normalizeText(post.tipoAnimal)
        val tipoNormalizado = getAnimalType(post.tipoAnimal)

        val tipoSeleccionado = when (tipoFiltro) {
            "Perros" -> "perro"
            "Gatos" -> "gato"
            else -> "todos"
        }

        val matchBusqueda =
            search.isBlank() ||
                    nombre.contains(search) ||
                    descripcion.contains(search) ||
                    ubicacion.contains(search) ||
                    estado.contains(search) ||
                    tipoOriginal.contains(search) ||
                    tipoNormalizado.contains(search)

        val matchTipo =
            tipoSeleccionado == "todos" ||
                    tipoNormalizado == tipoSeleccionado ||
                    tipoOriginal.contains(tipoSeleccionado)

        val matchUbicacion =
            ubicacionSearch.isBlank() ||
                    ubicacion.contains(ubicacionSearch)

        matchBusqueda && matchTipo && matchUbicacion
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = null,
                tint = MainPurple,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "FridaPawls",
                color = MainPurple,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    RoundedCornerShape(50)
                )
                .padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Buscar mascotas...")
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            FilterChip(
                selected = tipoFiltro == "Todos",
                onClick = { tipoFiltro = "Todos" },
                label = {
                    Text("Todos")
                }
            )

            FilterChip(
                selected = tipoFiltro == "Perros",
                onClick = { tipoFiltro = "Perros" },
                label = {
                    Text("Perros")
                }
            )

            FilterChip(
                selected = tipoFiltro == "Gatos",
                onClick = { tipoFiltro = "Gatos" },
                label = {
                    Text("Gatos")
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = ubicacionFiltro,
            onValueChange = { ubicacionFiltro = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Filtrar por ubicación")
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Mostrando ${filteredPosts.size} de ${posts.size} publicaciones",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (filteredPosts.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "No se encontraron mascotas",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF1C1C1E)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Intenta cambiar la búsqueda o los filtros.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {

                items(
                    items = filteredPosts,
                    key = { post -> post.id }
                ) { post ->

                    PostCard(
                        post = post,

                        onChatClick = {

                            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                            val receiverId = post.uid

                            if (currentUserId.isNullOrBlank() || receiverId.isBlank()) {
                                return@PostCard
                            }

                            val safeCurrent = currentUserId.trim()
                            val safeReceiver = receiverId.trim()

                            if (safeCurrent == safeReceiver) {
                                return@PostCard
                            }

                            val chatId = listOf(safeCurrent, safeReceiver)
                                .sorted()
                                .joinToString("_")

                            if (chatId.isBlank()) {
                                return@PostCard
                            }

                            val encodedChatId = Uri.encode(chatId)
                            val encodedReceiverId = Uri.encode(safeReceiver)

                            navController.navigate(
                                "chat?chatId=$encodedChatId&receiverId=$encodedReceiverId"
                            )
                        },

                        onEditClick = { postId ->
                            navController.navigate("edit_post/$postId")
                        },

                        onDeleteClick = { postToDelete ->

                            val deletedPost = postToDelete

                            posts.remove(deletedPost)

                            db.collection("posts")
                                .document(deletedPost.id)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Publicación eliminada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                .addOnFailureListener { error ->

                                    posts.add(deletedPost)

                                    Toast.makeText(
                                        context,
                                        error.message ?: "No se pudo eliminar la publicación",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                    )
                }

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
}