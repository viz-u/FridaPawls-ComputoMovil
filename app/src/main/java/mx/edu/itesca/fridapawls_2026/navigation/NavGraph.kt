package mx.edu.itesca.fridapawls_2026.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import mx.edu.itesca.fridapawls_2026.ui.screens.auth.LoginScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.auth.RegisterScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.auth.WelcomeScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.chat.ChatDetailScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.main.MainScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.post.CreatePostScreen
import mx.edu.itesca.fridapawls_2026.ui.screens.post.EditPostScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    NavHost(
        navController = navController,
        startDestination = if (auth.currentUser != null) "main_tabs" else "welcome"
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

        composable("main_tabs") {
            MainScreen(rootNavController = navController)
        }

        composable("post") {
            CreatePostScreen(navController)
        }

        composable("edit_post/{postId}") { backStackEntry ->

            val postId = backStackEntry.arguments?.getString("postId") ?: ""

            EditPostScreen(
                postId,
                navController
            )
        }

        composable("chat?chatId={chatId}&receiverId={receiverId}") { backStackEntry ->

            val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
            val receiverId = backStackEntry.arguments?.getString("receiverId") ?: ""
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            if (
                chatId.isBlank() ||
                receiverId.isBlank() ||
                currentUserId.isBlank()
            ) {
                return@composable
            }

            ChatDetailScreen(
                chatId = chatId,
                receiverId = receiverId,
                currentUserId = currentUserId
            )
        }
    }
}