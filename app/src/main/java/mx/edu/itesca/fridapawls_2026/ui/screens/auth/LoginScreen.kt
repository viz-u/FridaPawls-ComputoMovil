package mx.edu.itesca.fridapawls_2026.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue

@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text( text = "Inicia Sesión",
                textAlign = TextAlign.Center,
                modifier =  Modifier.padding(horizontal = 80.dp, vertical = 10.dp),
                fontSize = 20.sp,
                color = MainBlue
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineSmall,
            color = MainBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Correo Electrónico")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("example@example.com") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Contraseña")

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {

                val icon = if (passwordVisible)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff

                IconButton(
                    onClick = { passwordVisible = !passwordVisible }
                ) {
                    Icon(icon, contentDescription = "Ver contraseña")
                }
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        TextButton(
            onClick = { }
        ) {
            Text("Olvidé mi contraseña")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { navController.navigate("main") },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿No tienes una cuenta?",
                style = MaterialTheme.typography.bodyLarge
            )

            TextButton(
                onClick = { navController.navigate("register") },
                contentPadding = PaddingValues(start = 4.dp)
            ) {
                Text(
                    text = "Regístrate",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MainBlue
                )
            }
        }
    }
}