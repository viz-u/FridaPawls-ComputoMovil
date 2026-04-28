package mx.edu.itesca.fridapawls_2026.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import mx.edu.itesca.fridapawls_2026.R
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost
import mx.edu.itesca.fridapawls_2026.ui.components.PostCard
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@Composable
fun HomeScreen() {

    var searchText by remember { mutableStateOf("") }
    var filterNearby by remember { mutableStateOf(false) }

    val posts = remember {
        mutableStateListOf(
            MascotaPost(
                nombre = "Luna",
                descripcion = "Perrita de 3 meses.",
                ubicacion = "Ciudad Obregón",
                estado = "En adopción",
                imagenes = listOf(R.drawable.perro1, R.drawable.perro2)
            ),
            MascotaPost(
                nombre = "Michi",
                descripcion = "Gatito de 2 meses.",
                ubicacion = "Hermosillo",
                estado = "Rescate",
                imagenes = listOf(R.drawable.gato1)
            )
        )
    }

    val filteredPosts = posts.filter {
        it.nombre.contains(searchText, true) ||
                it.descripcion.contains(searchText, true)
    }.filter {
        if (!filterNearby) true
        else it.ubicacion.contains("Obregón", true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    )
                )
            )
    ) {

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            Spacer(modifier = Modifier.height(12.dp))

            // 🔥 HEADER (AQUÍ ESTÁ EL CAMBIO GRANDE)
            Text(
                text = "FridaPawls",
                style = MaterialTheme.typography.headlineMedium,
                color = MainPurple
            )

            Text(
                text = "Encuentra, adopta y ayuda 🐾",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🌫️ SEARCH CON EFECTO GLASS
            Box {

                Card(
                    shape = MaterialTheme.shapes.extraLarge,
                    elevation = CardDefaults.cardElevation(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {

                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            placeholder = { Text("Buscar mascotas...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            AssistChip(
                                onClick = { filterNearby = !filterNearby },
                                label = { Text("Cerca de mí") },
                                leadingIcon = {
                                    Icon(Icons.Default.LocationOn, contentDescription = null)
                                },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = if (filterNearby)
                                        MainPurple.copy(alpha = 0.2f)
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
                                )
                            )

                            Switch(
                                checked = filterNearby,
                                onCheckedChange = { filterNearby = it }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 🧱 FEED CON PROFUNDIDAD
            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {

                items(filteredPosts) { post ->

                    Card(
                        shape = MaterialTheme.shapes.extraLarge,
                        elevation = CardDefaults.cardElevation(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            PostCard(post)
                        }
                    }
                }
            }
        }
    }
}