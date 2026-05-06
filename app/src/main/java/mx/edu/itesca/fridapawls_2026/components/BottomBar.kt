package mx.edu.itesca.fridapawls_2026.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(
        BottomItem("home", Icons.Default.Home),
        BottomItem("chats", Icons.Default.Chat),
        BottomItem("profile", Icons.Default.Person)
    )

    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Box(
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Color(0xFF2A1B3D).copy(alpha = 0.92f) // 🔥 morado oscuro elegante
            )
            .padding(horizontal = 18.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { item ->

                val selected = currentRoute == item.route

                val scale by animateFloatAsState(
                    targetValue = if (selected) 1.2f else 1f,
                    label = ""
                )

                val color by animateColorAsState(
                    targetValue = if (selected)
                        Color(0xFFB388FF)
                    else
                        Color.White.copy(alpha = 0.6f),
                    label = ""
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clickable {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                }
                            }
                        }
                        .padding(6.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected)
                                    Color(0xFF7C4DFF).copy(alpha = 0.25f)
                                else
                                    Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = null,
                            tint = color
                        )
                    }

                    if (selected) {
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .height(3.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFB388FF))
                        )
                    }
                }
            }
        }
    }
}

private data class BottomItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)