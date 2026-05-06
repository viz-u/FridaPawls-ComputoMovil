package mx.edu.itesca.fridapawls_2026.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import mx.edu.itesca.fridapawls_2026.ui.screens.auth.*
import mx.edu.itesca.fridapawls_2026.ui.screens.main.*

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    // 🔥 escucha cambios de sesión
    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener {
            isLoggedIn = it.currentUser != null
        }

        auth.addAuthStateListener(listener)

        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "main" else "welcome"
    ) {

        composable("welcome") { WelcomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }

        composable("main") {
            MainScreen(navController)
        }

        composable("post") {
            CreatePostScreen(navController)
        }

        composable(
            "edit_post/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString("postId") ?: ""
            EditPostScreen(id, navController)
        }
    }
}