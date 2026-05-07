package mx.edu.itesca.fridapawls_2026.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCard(
    post: MascotaPost,
    onChatClick: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: (MascotaPost) -> Unit = {}
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val isOwner = currentUser?.uid == post.uid

    val pagerState = rememberPagerState(
        pageCount = { post.imagenes.size }
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {

            if (post.imagenes.isNotEmpty()) {

                HorizontalPager(
                    state = pagerState
                ) { page ->

                    AsyncImage(
                        model = post.imagenes[page],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),
                        contentScale = ContentScale.Crop
                    )
                }

            } else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sin imagen")
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
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

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MainPurple
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = post.ubicacion.ifBlank { "Sin ubicación" }
                            )
                        }

                        Text(
                            text = post.estado.ifBlank { "Sin estado" },
                            modifier = Modifier
                                .background(MainPurple, RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = post.nombreMascota.ifBlank { "Mascota sin nombre" },
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = post.descripcion.ifBlank { "Sin descripción" }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isOwner) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Button(
                                onClick = {
                                    if (post.id.isNotBlank()) {
                                        onEditClick(post.id)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MainPurple
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text("Editar")
                            }

                            Button(
                                onClick = {
                                    if (post.id.isNotBlank()) {
                                        onDeleteClick(post)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(6.dp))

                                Text("Eliminar")
                            }
                        }

                    } else {

                        Button(
                            onClick = {
                                onChatClick()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MainBlue
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Iniciar chat")
                        }
                    }
                }
            }
        }
    }
}