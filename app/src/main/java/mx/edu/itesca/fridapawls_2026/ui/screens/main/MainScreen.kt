package mx.edu.itesca.fridapawls_2026.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.edu.itesca.fridapawls_2026.components.BottomBar
import mx.edu.itesca.fridapawls_2026.ui.screens.chat.ChatsScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.profile.ProfileScreen

@Composable
fun MainScreen(rootNavController: NavHostController) {

    val innerNav = rememberNavController()

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomBar(innerNav)
        }
    ) { padding ->

        NavHost(
            navController = innerNav,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {

            composable("home") {
                HomeScreen(rootNavController)
            }

            composable("chats") {
                ChatsScreen(rootNavController)
            }

            composable("profile") {
                ProfileScreen(rootNavController)
            }
        }
    }
}