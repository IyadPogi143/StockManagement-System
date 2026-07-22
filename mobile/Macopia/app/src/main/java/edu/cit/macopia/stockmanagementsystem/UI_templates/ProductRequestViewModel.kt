package edu.cit.macopia.stockmanagementsystem.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cit.macopia.stockmanagementsystem.network.ProductChangeRequestResponse
import edu.cit.macopia.stockmanagementsystem.network.ReviewProductRequestBody
import edu.cit.macopia.stockmanagementsystem.network.RetrofitClient
import edu.cit.macopia.stockmanagementsystem.network.SubmitProductRequestBody
import kotlinx.coroutines.launch

class ProductRequestViewModel : ViewModel() {

    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)
    var submitSuccess = mutableStateOf(false)

    var myRequests = mutableStateOf<List<ProductChangeRequestResponse>>(emptyList())
    var queueRequests = mutableStateOf<List<ProductChangeRequestResponse>>(emptyList())

    // ---- Clerk: submit a new add/edit/delete request ----
    fun submitRequest(
        requestType: String,
        sku: String,
        productName: String?,
        category: String?,
        description: String?,
        quantity: Int?,
        minThreshold: Int?,
        userId: Long
    ) {
        errorMessage.value = null
        submitSuccess.value = false
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.submitProductRequest(
                    SubmitProductRequestBody(
                        requestType = requestType,
                        sku = sku,
                        productName = productName,
                        category = category,
                        description = description,
                        quantity = quantity,
                        minThreshold = minThreshold,
                        userId = userId
                    )
                )
                isLoading.value = false
                if (response.isSuccessful) {
                    submitSuccess.value = true
                } else {
                    errorMessage.value = response.errorBody()?.string() ?: "Failed to submit request"
                }
            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = "Network error: ${e.message}"
            }
        }
    }

    // ---- Clerk (or any user): view their own submitted requests ----
    fun loadMyRequests(userId: Long) {
        errorMessage.value = null
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getMyProductRequests(userId)
                isLoading.value = false
                if (response.isSuccessful) {
                    myRequests.value = response.body() ?: emptyList()
                } else {
                    errorMessage.value = "Failed to load your requests"
                }
            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = "Network error: ${e.message}"
            }
        }
    }

    // ---- Admin: load the review queue ----
    fun loadQueue(status: String? = "PENDING") {
        errorMessage.value = null
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getProductRequests(status)
                isLoading.value = false
                if (response.isSuccessful) {
                    queueRequests.value = response.body() ?: emptyList()
                } else {
                    errorMessage.value = "Failed to load requests"
                }
            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = "Network error: ${e.message}"
            }
        }
    }

    // ---- Admin: approve or reject with required feedback ----
    fun review(
        requestId: Long,
        decision: String,
        feedback: String,
        reviewedByUserId: Long,
        onDone: () -> Unit
    ) {
        errorMessage.value = null
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.reviewProductRequest(
                    requestId,
                    ReviewProductRequestBody(decision, feedback, reviewedByUserId)
                )
                isLoading.value = false
                if (response.isSuccessful) {
                    onDone()
                } else {
                    errorMessage.value = response.errorBody()?.string() ?: "Failed to submit decision"
                }
            } catch (e: Exception) {
                isLoading.value = false
                errorMessage.value = "Network error: ${e.message}"
            }
        }
    }
}
