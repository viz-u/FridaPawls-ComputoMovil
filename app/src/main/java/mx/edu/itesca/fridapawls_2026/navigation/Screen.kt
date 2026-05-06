package mx.edu.itesca.fridapawls_2026.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val icon: ImageVector
) {
    object Home : Screen("home", Icons.Default.Home)
    object Chats : Screen("chats", Icons.Default.Chat)
    object Profile : Screen("profile", Icons.Default.Person)
}