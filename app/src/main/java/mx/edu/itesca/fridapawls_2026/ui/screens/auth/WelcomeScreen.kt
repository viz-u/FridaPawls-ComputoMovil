package mx.edu.itesca.fridapawls_2026.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import mx.edu.itesca.fridapawls_2026.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple

@Composable
fun WelcomeScreen(navController: NavController) {

    Column(modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "Logo"
        )
        Text(text = "Bienvenido a FridaPawls. Encuentra, adopta y ayuda. Todo en un solo lugar.", textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MainBlue)
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MainPurple)

        ) {
            Text("Crear cuenta")
        }
    }
}