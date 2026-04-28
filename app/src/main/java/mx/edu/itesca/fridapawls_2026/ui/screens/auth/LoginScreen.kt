package mx.edu.itesca.fridapawls_2026.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        // 🔙 HEADER
        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Inicia Sesión",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 80.dp, vertical = 10.dp),
                fontSize = 20.sp,
                color = MainBlue
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 👋 BIENVENIDA
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineSmall,
            color = MainBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 📧 EMAIL
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

        // 🔐 PASSWORD
        Text("Contraseña")
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Ver contraseña"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        // 🔁 RECUPERAR PASSWORD
        TextButton(
            onClick = {

                if (email.isBlank()) {
                    Toast.makeText(context, "Ingresa tu correo primero", Toast.LENGTH_SHORT).show()
                } else {
                    auth.sendPasswordResetEmail(email)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }

            }
        ) {
            Text("Olvidé mi contraseña")
        }

        Spacer(modifier = Modifier.height(30.dp))

        // 🚀 BOTÓN LOGIN
        Button(
            onClick = {

                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true

                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        isLoading = false
                        Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()

                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    .addOnFailureListener {
                        isLoading = false
                        Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MainBlue),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Log In")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔁 REGISTRO
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