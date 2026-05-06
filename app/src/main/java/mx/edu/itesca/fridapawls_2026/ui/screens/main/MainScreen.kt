package mx.edu.itesca.fridapawls_2026.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import mx.edu.itesca.fridapawls_2026.components.BottomBar

@Composable
fun MainScreen(rootNavController: NavHostController) {

    val innerNavController = rememberNavController()

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            NavHost(
                navController = innerNavController,
                startDestination = "home",
                modifier = Modifier.fillMaxSize()
            ) {

                composable("home") {
                    HomeScreen(rootNavController)
                }

                composable("chats") {
                    ChatsScreen()
                }

                composable("profile") {
                    ProfileScreen(rootNavController)
                }
            }

            // 🔥 BottomBar flotante REAL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(androidx.compose.ui.Alignment.BottomCenter)
                    .padding(bottom = 18.dp)
            ) {
                BottomBar(innerNavController)
            }
        }
    }
}