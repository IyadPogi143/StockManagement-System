package edu.cit.macopia.stockmanagementsystem.network

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val role: String,
    val message: String
)

// ---- Product Change Requests ----

data class ProductChangeRequestResponse(
    val requestId: Long,
    val requestType: String, // "CREATE", "UPDATE", "DELETE"
    val sku: String,
    val proposedProductName: String?,
    val proposedCategory: String?,
    val proposedDescription: String?,
    val proposedQuantity: Int?,
    val proposedMinThreshold: Int?,
    val requestedByUsername: String,
    val status: String, // "PENDING", "APPROVED", "REJECTED"
    val adminFeedback: String?,
    val reviewedByUsername: String?,
    val createdAt: String,
    val reviewedAt: String?
)

data class SubmitProductRequestBody(
    val requestType: String,
    val sku: String,
    val productName: String?,
    val category: String?,
    val description: String?,
    val quantity: Int?,
    val minThreshold: Int?,
    val userId: Long
)

data class ReviewProductRequestBody(
    val decision: String, // "APPROVE" or "REJECT"
    val feedback: String,
    val reviewedByUserId: Long
)

// ---- Product Catalog (Administrator) ----

data class ProductResponse(
    val sku: String,
    val productName: String,
    val category: String,
    val description: String?,
    val quantity: Int,
    val minThreshold: Int,
    val lowStock: Boolean
)

data class AdjustQuantityBody(
    val changeAmount: Int,
    val userId: Long
)

// ---- Registered Users (Administrator) ----

data class UserSummaryResponse(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val username: String,
    val email: String,
    val role: String,
    val dateCreated: String
)