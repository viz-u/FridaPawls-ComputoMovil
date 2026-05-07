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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.edu.itesca.fridapawls_2026.network.CloudinaryService
import mx.edu.itesca.fridapawls_2026.ui.components.PostForm
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@Composable
fun EditPostScreen(postId: String, navController: NavController) {

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()
    val cloudinary = remember { CloudinaryService() }

    // 🧠 STATE
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Adopción") }
    var tipoAnimal by remember { mutableStateOf("") }

    // 🔥 EXISTENTES + NUEVAS
    var existingImages by remember { mutableStateOf<List<String>>(emptyList()) }
    var newImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var isLoading by remember { mutableStateOf(false) }

    // 📥 PICKER MULTI
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->

        // ✔ ACUMULA NUEVAS
        newImages = newImages + uris
    }

    // 📦 CARGAR POST
    LaunchedEffect(postId) {
        val doc = db.collection("posts").document(postId).get().await()

        nombre = doc.getString("nombreMascota").orEmpty()
        descripcion = doc.getString("descripcion").orEmpty()
        ubicacion = doc.getString("ubicacion").orEmpty()
        estado = doc.getString("estado").orEmpty()
        tipoAnimal = doc.getString("tipoAnimal").orEmpty()

        existingImages = doc.get("imagenes") as? List<String> ?: emptyList()
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp)
    ) {

        PostForm(

            nombre = nombre,
            descripcion = descripcion,
            ubicacion = ubicacion,
            estado = estado,
            tipoAnimal = tipoAnimal,

            // 👇 mostramos existentes + nuevas (preview mixto)
            imagenes = newImages,

            onNombreChange = { nombre = it },
            onDescripcionChange = { descripcion = it },
            onUbicacionChange = { ubicacion = it },
            onEstadoChange = { estado = it },
            onTipoAnimalChange = { tipoAnimal = it },

            onRemoveImage = { uri ->
                newImages = newImages - uri
            },

            onImagesClick = {
                if (!isLoading) picker.launch("image/*")
            }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {

                if (isLoading) return@Button
                isLoading = true

                scope.launch {

                    try {

                        // ☁️ subir solo nuevas imágenes
                        val uploaded = newImages.map {
                            cloudinary.uploadImage(context, it)
                        }

                        // 🔥 combinar existentes + nuevas
                        val finalImages = existingImages + uploaded

                        val update = mapOf(
                            "nombreMascota" to nombre,
                            "descripcion" to descripcion,
                            "ubicacion" to ubicacion,
                            "estado" to estado,
                            "tipoAnimal" to tipoAnimal,
                            "imagenes" to finalImages
                        )

                        db.collection("posts")
                            .document(postId)
                            .update(update)
                            .await()

                        Toast.makeText(context, "Actualizado", Toast.LENGTH_SHORT).show()
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
                Text("Guardar cambios")
            }
        }
    }
}