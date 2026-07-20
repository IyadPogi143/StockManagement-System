package edu.cit.macopia.stockmanagementsystem.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Amber, shape = MaterialTheme.shapes.medium),
            contentAlignment = Alignment.Center
        ) {
            Text("M", color = Navy, fontWeight = FontWeight.Bold, fontSize = 28.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("MACOPIA", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Navy)

        Spacer(modifier = Modifier.height(32.dp))
        Text("Welcome back", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Navy)
        Text("Login to your Macopia account", color = TextGrey, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        viewModel.errorMessage.value?.let {
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                viewModel.login(username, password) {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Navy),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = !viewModel.isLoading.value
        ) {
            if (viewModel.isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Amber)
            } else {
                Text("Sign In")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("No account? Register", color = Navy)
        }
    }
}