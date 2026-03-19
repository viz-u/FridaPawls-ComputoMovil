package mx.edu.itesca.fridapawls_2026.ui.screens.main

import android.provider.ContactsContract
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.edu.itesca.fridapawls_2026.navigation.Screen
import mx.edu.itesca.fridapawls_2026.components.BottomBar

@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) },
        containerColor = Color.White
    ){ padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) { HomeScreen() }
            composable(Screen.Chats.route) { ChatsScreen() }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable("post") {CreatePostScreen(navController)}
        }
    }
}