package edu.cit.macopia.stockmanagementsystem.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.macopia.stockmanagementsystem.network.AuthResponse
import edu.cit.macopia.stockmanagementsystem.network.LoginRequest
import edu.cit.macopia.stockmanagementsystem.network.RegisterRequest
import edu.cit.macopia.stockmanagementsystem.network.RetrofitClient
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var loggedInUser = mutableStateOf<AuthResponse?>(null)

    fun register(
        firstName: String,
        lastName: String,
        middleName: String,
        username: String,
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        errorMessage.value = null
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.register(
                    RegisterRequest(firstName, lastName, middleName.ifBlank { null }, username, email, password)
                )
                isLoading.value = false
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    errorMessage.value = response.errorBody()?.string() ?: "Registration failed"
                }
            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun login(username: String, password: String, onSuccess: () -> Unit) {
        errorMessage.value = null
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(username, password))
                isLoading.value = false
                if (response.isSuccessful) {
                    loggedInUser.value = response.body()
                    onSuccess()
                } else {
                    errorMessage.value = response.errorBody()?.string() ?: "Login failed"
                }
            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = "Network error: ${e.message}"
            }
        }
    }
}