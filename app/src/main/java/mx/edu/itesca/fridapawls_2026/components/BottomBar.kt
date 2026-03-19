package mx.edu.itesca.fridapawls_2026.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mx.edu.itesca.fridapawls_2026.navigation.Screen
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue

@Composable
fun BottomBar(navController: NavController) {

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp, 0.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MainBlue),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            // HOME
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "Home",
                tint = if (currentRoute == Screen.Home.route) Color.White else Color(0xFFE0D7FF),
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
            )

            // CHATS
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = "Chats",
                tint = if (currentRoute == Screen.Chats.route) Color.White else Color(0xFFE0D7FF),
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        navController.navigate(Screen.Chats.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
            )

            // PROFILE
            Icon(
                imageVector = Icons.Outlined.PersonOutline,
                contentDescription = "Profile",
                tint = if (currentRoute == Screen.Profile.route) Color.White else Color(0xFFE0D7FF),
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
            )
        }
    }
}