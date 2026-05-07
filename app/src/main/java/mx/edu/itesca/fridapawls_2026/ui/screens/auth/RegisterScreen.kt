package mx.edu.itesca.fridapawls_2026.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import mx.edu.itesca.fridapawls_2026.ui.theme.MainBlue
import mx.edu.itesca.fridapawls_2026.ui.theme.MainPurple
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var fechaNacimientoMillis by remember { mutableStateOf<Long?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF7F7FB)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(42.dp))

            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(CircleShape)
                    .background(MainPurple.copy(alpha = 0.13f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    tint = MainPurple,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1C1C1E)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Únete a FridaPawls para ayudar a más mascotas",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF7A7A7A)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Nombre de usuario")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF8E8E93)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    colors = inputColors()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Correo electrónico")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = Color(0xFF8E8E93)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    colors = inputColors()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { value ->
                        telefono = value.filter { it.isDigit() }.take(10)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Número de teléfono")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = Color(0xFF8E8E93)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    colors = inputColors()
                )

                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker = true
                        },
                    enabled = false,
                    placeholder = {
                        Text("Fecha de nacimiento")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = Color(0xFF8E8E93)
                        )
                    },
                    shape = RoundedCornerShape(22.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledContainerColor = Color.White,
                        disabledBorderColor = Color(0xFFE7E7EE),
                        disabledPlaceholderColor = Color(0xFF8E8E93),
                        disabledLeadingIconColor = Color(0xFF8E8E93),
                        disabledTextColor = Color(0xFF1C1C1E)
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Contraseña")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color(0xFF8E8E93)
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = null,
                                tint = Color(0xFF8E8E93)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(22.dp),
                    colors = inputColors()
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MainPurple,
                    disabledContainerColor = MainPurple.copy(alpha = 0.45f)
                ),
                onClick = {

                    if (
                        nombre.isBlank() ||
                        email.isBlank() ||
                        telefono.isBlank() ||
                        fechaNacimiento.isBlank() ||
                        fechaNacimientoMillis == null ||
                        password.isBlank()
                    ) {
                        Toast.makeText(
                            context,
                            "Completa todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (telefono.length < 10) {
                        Toast.makeText(
                            context,
                            "Ingresa un número de teléfono válido",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (password.length < 6) {
                        Toast.makeText(
                            context,
                            "La contraseña debe tener mínimo 6 caracteres",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (!isAdult(fechaNacimientoMillis!!)) {
                        Toast.makeText(
                            context,
                            "Debes ser mayor de edad para crear una cuenta",
                            Toast.LENGTH_LONG
                        ).show()

                        navController.navigate("welcome") {
                            popUpTo("register") {
                                inclusive = true
                            }
                        }

                        return@Button
                    }

                    isLoading = true

                    auth.createUserWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener { result ->

                            val user = result.user

                            if (user == null) {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "No se pudo crear la cuenta",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@addOnSuccessListener
                            }

                            val data = hashMapOf(
                                "uid" to user.uid,
                                "nombre" to nombre.trim(),
                                "email" to email.trim(),
                                "telefono" to telefono.trim(),
                                "fechaNacimiento" to fechaNacimiento,
                                "fechaNacimientoMillis" to fechaNacimientoMillis,
                                "createdAt" to System.currentTimeMillis()
                            )

                            db.collection("users")
                                .document(user.uid)
                                .set(data)
                                .addOnSuccessListener {

                                    isLoading = false

                                    navController.navigate("main_tabs") {
                                        popUpTo("register") {
                                            inclusive = true
                                        }
                                    }
                                }
                                .addOnFailureListener {
                                    isLoading = false

                                    Toast.makeText(
                                        context,
                                        it.message ?: "Error al guardar usuario",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }
                        .addOnFailureListener {
                            isLoading = false

                            Toast.makeText(
                                context,
                                it.message ?: "Error al crear cuenta",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
            ) {

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Crear cuenta",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "¿Ya tienes cuenta?",
                    color = Color(0xFF7A7A7A),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = " Inicia sesión",
                    modifier = Modifier.clickable {
                        navController.navigate("login")
                    },
                    color = MainBlue,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showDatePicker) {

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis

                        if (selectedMillis != null) {
                            fechaNacimientoMillis = selectedMillis
                            fechaNacimiento = formatDate(selectedMillis)
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar", color = MainPurple)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@Composable
private fun inputColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedBorderColor = MainPurple.copy(alpha = 0.45f),
    unfocusedBorderColor = Color(0xFFE7E7EE),
    cursorColor = MainPurple
)

private fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date(millis))
}

private fun isAdult(birthDateMillis: Long): Boolean {
    val today = Calendar.getInstance()

    val birthDate = Calendar.getInstance().apply {
        timeInMillis = birthDateMillis
    }

    var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

    val currentMonth = today.get(Calendar.MONTH)
    val birthMonth = birthDate.get(Calendar.MONTH)

    val currentDay = today.get(Calendar.DAY_OF_MONTH)
    val birthDay = birthDate.get(Calendar.DAY_OF_MONTH)

    if (
        currentMonth < birthMonth ||
        currentMonth == birthMonth && currentDay < birthDay
    ) {
        age--
    }

    return age >= 18
}