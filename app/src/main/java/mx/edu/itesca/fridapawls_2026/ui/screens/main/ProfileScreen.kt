package mx.edu.itesca.fridapawls_2026.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import mx.edu.itesca.fridapawls_2026.R
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple
import mx.edu.itesca.fridapawls_2026.ui.components.PostCard
import mx.edu.itesca.fridapawls_2026.ui.components.ProfileStat
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue

@Composable
fun ProfileScreen(navController: NavController) {

    // 🐾 posts de ejemplo del usuario
    val myPosts = remember {
        listOf(
            MascotaPost(
                nombre = "Luna",
                descripcion = "Perrita de 3 meses.",
                ubicacion = "Ciudad Obregón",
                estado = "En adopción",
                imagenes = listOf(R.drawable.perro1)
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {

            // 👤 PERFIL
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(id = R.drawable.applogo),
                    contentDescription = "Foto perfil",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "David Arvizu",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Text(
                    text = "david@email.com",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 📊 STATS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStat("Posts", myPosts.size.toString())
                    ProfileStat("Likes", "120")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 🚪 BOTÓN
                Button(
                    onClick = { navController.navigate("welcome") },
                    colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
                ) {
                    Text("Cerrar sesión")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navController.navigate("post") },
                    colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
                ) {
                    Text("Crear publicación")
                }
                Spacer(modifier = Modifier.height(20.dp))

            }
        }

        // 🐾 MIS PUBLICACIONES
        items(myPosts) { post ->
            PostCard(post)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}