package edu.cit.macopia.stockmanagementsystem.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // ---- Product Change Requests ----

    @POST("api/product-requests")
    suspend fun submitProductRequest(@Body request: SubmitProductRequestBody): Response<ProductChangeRequestResponse>

    // A user's own submitted requests, for the clerk's "My Requests" screen
    @GET("api/product-requests/mine")
    suspend fun getMyProductRequests(@Query("userId") userId: Long): Response<List<ProductChangeRequestResponse>>

    // The admin review queue; status is optional (PENDING/APPROVED/REJECTED), omit for all
    @GET("api/product-requests")
    suspend fun getProductRequests(@Query("status") status: String? = null): Response<List<ProductChangeRequestResponse>>

    @PATCH("api/product-requests/{requestId}/review")
    suspend fun reviewProductRequest(
        @Path("requestId") requestId: Long,
        @Body request: ReviewProductRequestBody
    ): Response<ProductChangeRequestResponse>

    // ---- Product Catalog (Administrator) ----

    @GET("api/products")
    suspend fun getProducts(): Response<List<ProductResponse>>

    @PATCH("api/products/{sku}/quantity")
    suspend fun adjustQuantity(
        @Path("sku") sku: String,
        @Body request: AdjustQuantityBody
    ): Response<ProductResponse>

    // ---- Registered Users (Administrator) ----

    @GET("api/users")
    suspend fun getUsers(): Response<List<UserSummaryResponse>>
}
