package mx.edu.itesca.fridapawls_2026.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Welcome : Screen("welcome")

    object Main : Screen("main") // contenedor con bottom nav

    object Home : Screen("home")
    object Chats : Screen("chats")
    object Profile : Screen("profile")
}