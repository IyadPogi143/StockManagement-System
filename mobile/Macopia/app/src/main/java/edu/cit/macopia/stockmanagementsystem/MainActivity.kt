package edu.cit.macopia.stockmanagementsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.cit.macopia.stockmanagementsystem.ui.AdminViewModel
import edu.cit.macopia.stockmanagementsystem.ui.AuthViewModel
import edu.cit.macopia.stockmanagementsystem.ui.DashboardScreen
import edu.cit.macopia.stockmanagementsystem.ui.LoginScreen
import edu.cit.macopia.stockmanagementsystem.ui.MyRequestsScreen
import edu.cit.macopia.stockmanagementsystem.ui.ProductCatalogScreen
import edu.cit.macopia.stockmanagementsystem.ui.ProductRequestViewModel
import edu.cit.macopia.stockmanagementsystem.ui.RegisterScreen
import edu.cit.macopia.stockmanagementsystem.ui.ReviewRequestsScreen
import edu.cit.macopia.stockmanagementsystem.ui.SubmitProductRequestScreen
import edu.cit.macopia.stockmanagementsystem.ui.UsersListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    // Shared across all product-request screens so state (loading, lists) doesn't
    // reset every time you navigate between Submit / My Requests / Review.
    val productRequestViewModel: ProductRequestViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { navController.navigate("dashboard") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.navigate("login") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("dashboard") {
            DashboardScreen(
                viewModel = authViewModel,
                onLogout = { navController.navigate("login") },
                onSubmitRequest = { navController.navigate("submitRequest") },
                onMyRequests = { navController.navigate("myRequests") },
                onReviewRequests = { navController.navigate("reviewRequests") },
                onProductCatalog = { navController.navigate("productCatalog") },
                onUsersList = { navController.navigate("usersList") }
            )
        }
        composable("productCatalog") {
            val userId = authViewModel.loggedInUser.value?.userId ?: 0L
            ProductCatalogScreen(
                viewModel = adminViewModel,
                userId = userId,
                onBack = { navController.popBackStack() }
            )
        }
        composable("usersList") {
            UsersListScreen(
                viewModel = adminViewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("submitRequest") {
            val userId = authViewModel.loggedInUser.value?.userId ?: 0L
            SubmitProductRequestScreen(
                viewModel = productRequestViewModel,
                userId = userId,
                onBack = { navController.popBackStack() },
                onViewMyRequests = { navController.navigate("myRequests") }
            )
        }
        composable("myRequests") {
            val userId = authViewModel.loggedInUser.value?.userId ?: 0L
            MyRequestsScreen(
                viewModel = productRequestViewModel,
                userId = userId,
                onBack = { navController.popBackStack() },
                onNewRequest = { navController.navigate("submitRequest") }
            )
        }
        composable("reviewRequests") {
            val userId = authViewModel.loggedInUser.value?.userId ?: 0L
            ReviewRequestsScreen(
                viewModel = productRequestViewModel,
                reviewerUserId = userId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
