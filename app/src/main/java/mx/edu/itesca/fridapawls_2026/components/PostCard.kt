package mx.edu.itesca.fridapawls_2026.ui.components

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.itesca.fridapawls_2026.data.model.MascotaPost
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@Composable
fun PostCard(post: MascotaPost) {

    var liked by remember { mutableStateOf(post.liked) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MainPurple.copy(alpha = 0.5f))

    ) {

        Column {

            Column(modifier = Modifier.padding(12.dp)) {
                Text(post.nombre, fontWeight = FontWeight.Bold)
                Text(post.ubicacion, fontSize = 12.sp, color = Color.Black)
            }

            LazyRow {
                items(post.imagenes) { img ->
                    Image(
                        painter = painterResource(id = img),
                        contentDescription = null,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .height(250.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row {
                    IconButton(onClick = { liked = !liked }) {
                        Icon(
                            imageVector = if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (liked) Color.Red else Color.Black
                        )
                    }

                    IconButton(onClick = { /* abrir chat */ }) {
                        Icon(Icons.Default.ChatBubbleOutline, contentDescription = null)
                    }
                }

                Text(
                    text = post.estado,
                    color = Color.White,
                    modifier = Modifier
                        .background(MainBlue.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}