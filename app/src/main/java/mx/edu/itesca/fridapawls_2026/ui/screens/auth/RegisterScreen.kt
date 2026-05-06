package mx.edu.itesca.fridapawls_2026.ui.screens.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import com.google.firebase.firestore.FirebaseFirestore
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var nacimiento by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        // HEADER
        Row(verticalAlignment = Alignment.CenterVertically) {

            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Crear Cuenta",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 80.dp, vertical = 10.dp),
                fontSize = 20.sp,
                color = MainBlue
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // NOMBRE
        Text("Nombre Completo")
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Escribe tu nombre...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // PASSWORD
        Text("Contraseña")
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("********") },
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

        Spacer(modifier = Modifier.height(12.dp))

        // EMAIL
        Text("Correo Electrónico")
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("example@example.com") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // CELULAR
        Text("Número Celular")
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = celular,
            onValueChange = { celular = it },
            placeholder = { Text("Ingresa tu número...") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // FECHA
        Text("Fecha de Nacimiento")
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nacimiento,
            onValueChange = {},
            label = { Text("Selecciona tu fecha") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            readOnly = true
        )

        // DATE PICKER
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            nacimiento = date.toString()
                        }
                        showDatePicker = false
                    }) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // BOTÓN REGISTRO
        Button(
            onClick = {

                if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (password.length < 6) {
                    Toast.makeText(context, "Mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->

                        val uid = result.user!!.uid

                        val user = hashMapOf(
                            "uid" to uid,
                            "nombre" to nombre,
                            "email" to email,
                            "celular" to celular,
                            "nacimiento" to nacimiento
                        )

                        // 🔥 GUARDAR EN FIRESTORE
                        db.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d("FIRESTORE", "Usuario guardado correctamente")

                                isLoading = false

                                // 🚀 SOLO NAVEGA SI TODO SALIÓ BIEN
                                navController.navigate("main") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                            .addOnFailureListener { e ->
                                isLoading = false
                                Log.e("FIRESTORE", "Error al guardar", e)
                                Toast.makeText(context, "Error al guardar usuario", Toast.LENGTH_LONG).show()
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
                Text("Crear cuenta")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("¿Ya tienes una cuenta?")

            TextButton(onClick = {
                navController.navigate("login")
            }) {
                Text("Iniciar Sesión")
            }
        }
    }
}