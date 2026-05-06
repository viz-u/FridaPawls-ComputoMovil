package mx.edu.itesca.fridapawls_2026.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@Composable
fun PostCard(
    post: MascotaPost,
    onChatClick: () -> Unit = {},
    onEditClick: (String) -> Unit = {}
) {

    val currentUser = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()

    val isOwner = currentUser?.uid == post.uid

    val pagerState = rememberPagerState { post.imagenes.size }

    Card(
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column {

            // 🖼️ CARRUSEL
            HorizontalPager(state = pagerState) { page ->
                AsyncImage(
                    model = post.imagenes[page],
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // 🌈 DEGRADADO INFERIOR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                androidx.compose.ui.graphics.Color.Transparent,
                                MainPurple.copy(alpha = 0.2f),
                                MainBlue.copy(alpha = 0.4f)
                            )
                        )
                    )
                    .padding(14.dp)
            ) {

                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Row {
                            Icon(Icons.Default.LocationOn, null, tint = MainPurple)
                            Spacer(Modifier.width(4.dp))
                            Text(post.ubicacion)
                        }

                        Text(
                            post.estado,
                            modifier = Modifier
                                .background(MainPurple, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(post.nombreMascota, fontWeight = FontWeight.Bold)

                    Spacer(Modifier.height(6.dp))

                    Text(post.descripcion)

                    Spacer(Modifier.height(12.dp))

                    // 🔥 ACCIONES
                    if (isOwner) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            // ✏️ EDITAR
                            Button(
                                onClick = { onEditClick(post.id) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MainPurple)
                            ) {
                                Icon(Icons.Default.Edit, null)
                                Spacer(Modifier.width(6.dp))
                                Text("Editar")
                            }

                            // 🗑️ ELIMINAR
                            Button(
                                onClick = {
                                    db.collection("posts")
                                        .document(post.id)
                                        .delete()
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(Icons.Default.Delete, null)
                                Spacer(Modifier.width(6.dp))
                                Text("Eliminar")
                            }
                        }

                    } else {

                        Button(
                            onClick = onChatClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(MainBlue)
                        ) {
                            Icon(Icons.Default.Chat, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Iniciar chat")
                        }
                    }
                }
            }
        }
    }
}