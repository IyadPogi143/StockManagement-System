package edu.cit.macopia.stockmanagementsystem.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.macopia.stockmanagementsystem.network.AdjustQuantityBody
import edu.cit.macopia.stockmanagementsystem.network.ProductResponse
import edu.cit.macopia.stockmanagementsystem.network.RetrofitClient
import edu.cit.macopia.stockmanagementsystem.network.UserSummaryResponse
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    var products = mutableStateOf<List<ProductResponse>>(emptyList())
    var users = mutableStateOf<List<UserSummaryResponse>>(emptyList())

    fun loadProducts() {
        errorMessage.value = null
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getProducts()
                isLoading.value = false
                if (response.isSuccessful) {
                    products.value = response.body() ?: emptyList()
                } else {
                    errorMessage.value = "Failed to load products"
                }
            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun adjustQuantity(sku: String, changeAmount: Int, userId: Long) {
        errorMessage.value = null
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.adjustQuantity(sku, AdjustQuantityBody(changeAmount, userId))
                if (response.isSuccessful) {
                    loadProducts() // refresh the list so the new quantity/lowStock flag shows immediately
                } else {
                    errorMessage.value = response.errorBody()?.string() ?: "Failed to adjust quantity"
                }
            } catch (e: Exception) {
                errorMessage.value = "Network error: ${e.message}"
            }
        }
    }

    fun loadUsers() {
        errorMessage.value = null
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUsers()
                isLoading.value = false
                if (response.isSuccessful) {
                    users.value = response.body() ?: emptyList()
                } else {
                    errorMessage.value = "Failed to load users"
                }
            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = "Network error: ${e.message}"
            }
        }
    }
}
