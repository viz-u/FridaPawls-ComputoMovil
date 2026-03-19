package mx.edu.itesca.fridapawls_2026.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import mx.edu.itesca.fridapawls_2026.ui.screens.auth.WelcomeScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.auth.LoginScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.main.MainScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.auth.RegisterScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.main.CreatePostScreen
@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
    ) {

        composable("welcome") {
            WelcomeScreen(navController)
        }

        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable("post") {
            CreatePostScreen(navController)
        }

        // 🔥
        composable("main") {
            MainScreen()
        }
    }
}