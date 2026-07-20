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