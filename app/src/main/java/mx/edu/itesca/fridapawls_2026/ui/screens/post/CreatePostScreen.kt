package mx.edu.itesca.fridapawls_2026.ui.screens.post

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.edu.itesca.fridapawls_2026.network.CloudinaryService
import mx.edu.itesca.fridapawls_2026.ui.components.PostForm
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@Composable
fun CreatePostScreen(navController: NavController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()
    val cloudinary = remember { CloudinaryService() }

    // 🧠 STATE
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Adopción") }

    // 🔥 IMPORTANTE: ESTO ES LO QUE ROMPE FILTROS SI NO ES CONSISTENTE
    var tipoAnimal by remember { mutableStateOf("") }

    var imagenes by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // 📸 MULTI IMAGE PICKER
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        imagenes = imagenes + uris
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        PostForm(

            nombre = nombre,
            descripcion = descripcion,
            ubicacion = ubicacion,
            estado = estado,
            tipoAnimal = tipoAnimal,

            imagenes = imagenes,

            onNombreChange = { nombre = it },
            onDescripcionChange = { descripcion = it },
            onUbicacionChange = { ubicacion = it },
            onEstadoChange = { estado = it },

            // 🔥 NORMALIZACIÓN IMPORTANTE
            onTipoAnimalChange = {
                tipoAnimal = it.trim().lowercase() // <- CLAVE PARA FILTROS
            },

            onRemoveImage = { uri ->
                imagenes = imagenes - uri
            },

            onImagesClick = {
                if (!isLoading) picker.launch("image/*")
            }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {

                if (nombre.isBlank() || descripcion.isBlank()) {
                    Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (imagenes.isEmpty()) {
                    Toast.makeText(context, "Agrega imágenes", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (tipoAnimal.isBlank()) {
                    Toast.makeText(context, "Selecciona tipo de animal", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true

                scope.launch {

                    try {

                        val urls = imagenes.map { uri ->
                            cloudinary.uploadImage(context, uri)
                        }

                        val post = hashMapOf(
                            "nombreMascota" to nombre,
                            "descripcion" to descripcion,
                            "ubicacion" to ubicacion,
                            "estado" to estado,
                            "tipoAnimal" to tipoAnimal, // 🔥 ya normalizado
                            "imagenes" to urls,
                            "uid" to auth.currentUser?.uid,
                            "timestamp" to Timestamp.now(),
                            "likes" to 0
                        )

                        db.collection("posts").add(post).await()

                        Toast.makeText(context, "Publicado", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()

                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(MainPurple)
        ) {

            if (isLoading) {
                CircularProgressIndicator(Modifier.size(20.dp))
            } else {
                Text("Publicar")
            }
        }
    }
}