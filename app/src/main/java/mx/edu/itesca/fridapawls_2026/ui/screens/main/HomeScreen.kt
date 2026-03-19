package mx.edu.itesca.fridapawls_2026.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.edu.itesca.fridapawls_2026.R
import mx.edu.itesca.fridapawls_2026.ui.components.PostCard
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost
import mx.edu.itesca.fridapawls_2026.ui.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    var searchText by remember { mutableStateOf("") }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        SearchBar(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

        LazyColumn {
            items(posts) { post ->
                PostCard(post)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}