package mx.edu.itesca.fridapawls_2026.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostForm(
    nombre: String,
    descripcion: String,
    ubicacion: String,
    estado: String,
    imagenes: List<Uri>,

    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onUbicacionChange: (String) -> Unit,
    onEstadoChange: (String) -> Unit,
    onImagesClick: () -> Unit   // 🔥 IMPORTANTE
) {

    val opciones = listOf("Adopción", "Rescate", "Hogar Temporal", "Cuidado Temporal")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {

        Text(
            text = "Nueva publicación",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MainPurple
        )

        Spacer(Modifier.height(12.dp))

        // 📸 IMÁGENES ARRIBA (IG STYLE)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MainPurple.copy(alpha = 0.08f))
                .clickable { onImagesClick() },
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {

            if (imagenes.isEmpty()) {
                Text("Toca para agregar imágenes", color = MainPurple)
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(imagenes) { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(2.dp)
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        // 🟣 CAMPOS
        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MainPurple,
                focusedLabelColor = MainPurple
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = onDescripcionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            minLines = 3,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MainPurple
            )
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = ubicacion,
            onValueChange = onUbicacionChange,
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MainPurple
            )
        )

        Spacer(Modifier.height(10.dp))

        // 📌 DROPDOWN
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {

            OutlinedTextField(
                value = estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de publicación") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                opciones.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            onEstadoChange(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}