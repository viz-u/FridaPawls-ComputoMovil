package mx.edu.itesca.fridapawls_2026.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostForm(
    nombre: String,
    descripcion: String,
    ubicacion: String,
    estado: String,
    tipoAnimal: String,
    imagenes: List<Uri>,

    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onUbicacionChange: (String) -> Unit,
    onEstadoChange: (String) -> Unit,
    onTipoAnimalChange: (String) -> Unit,

    onRemoveImage: (Uri) -> Unit,
    onImagesClick: () -> Unit
) {
    val opcionesEstado = listOf("Adopción", "Rescate", "Temporal")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {

        Text(
            text = "Nueva publicación",
            color = Color(0xFF1C1C1E),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Comparte la información de la mascota",
            color = Color(0xFF7A7A7A),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(MainPurple.copy(alpha = 0.08f))
                .clickable { onImagesClick() },
            contentAlignment = Alignment.Center
        ) {

            if (imagenes.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = null,
                        tint = MainPurple,
                        modifier = Modifier.size(38.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Agregar imágenes",
                        color = MainPurple,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(imagenes) { uri ->

                        Box(
                            modifier = Modifier.padding(4.dp)
                        ) {

                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(86.dp)
                                    .clip(RoundedCornerShape(14.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black.copy(alpha = 0.55f))
                                    .clickable { onRemoveImage(uri) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text("Nombre de la mascota") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Title,
                    contentDescription = null,
                    tint = Color(0xFF8E8E93)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = postInputColors()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = onDescripcionChange,
            label = { Text("Descripción") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = Color(0xFF8E8E93)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            shape = RoundedCornerShape(18.dp),
            colors = postInputColors()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = ubicacion,
            onValueChange = onUbicacionChange,
            label = { Text("Ubicación") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color(0xFF8E8E93)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            colors = postInputColors()
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Tipo de animal",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1C1C1E)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            FilterChip(
                selected = tipoAnimal == "perro",
                onClick = {
                    onTipoAnimalChange("perro")
                },
                label = {
                    Text("Perro")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MainPurple.copy(alpha = 0.16f),
                    selectedLabelColor = MainPurple,
                    selectedLeadingIconColor = MainPurple
                )
            )

            FilterChip(
                selected = tipoAnimal == "gato",
                onClick = {
                    onTipoAnimalChange("gato")
                },
                label = {
                    Text("Gato")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MainBlue.copy(alpha = 0.16f),
                    selectedLabelColor = MainBlue,
                    selectedLeadingIconColor = MainBlue
                )
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {

            OutlinedTextField(
                value = estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = postInputColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {

                opcionesEstado.forEach { opcion ->
                    DropdownMenuItem(
                        text = {
                            Text(opcion)
                        },
                        onClick = {
                            onEstadoChange(opcion)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun postInputColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedBorderColor = MainPurple.copy(alpha = 0.45f),
    unfocusedBorderColor = Color(0xFFE7E7EE),
    cursorColor = MainPurple
)