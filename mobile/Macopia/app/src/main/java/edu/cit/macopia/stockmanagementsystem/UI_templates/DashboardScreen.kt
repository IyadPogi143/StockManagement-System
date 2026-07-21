package edu.cit.macopia.stockmanagementsystem.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    viewModel: AuthViewModel,
    onLogout: () -> Unit,
    onSubmitRequest: () -> Unit,
    onMyRequests: () -> Unit,
    onReviewRequests: () -> Unit,
    onProductCatalog: () -> Unit,
    onUsersList: () -> Unit
) {
    val user = viewModel.loggedInUser.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("MACOPIA", color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            TextButton(onClick = onLogout) {
                Text("Logout", color = Amber)
            }
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text("Welcome, ${user?.firstName} ${user?.lastName}", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Navy)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Role: ${user?.role}", color = TextGrey, fontSize = 14.sp)
            Text("Email: ${user?.email}", color = TextGrey, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(28.dp))

            if (user?.role == "INVENTORY_CLERK") {
                Text(
                    "You can submit requests to add, edit, or delete products. An Administrator will " +
                        "review each request and leave feedback before it takes effect.",
                    color = TextGrey, fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSubmitRequest,
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Submit Product Request", color = Navy, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onMyRequests,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("My Requests", color = Navy)
                }
            }

            if (user?.role == "ADMINISTRATOR") {
                Text(
                    "View the product catalog, adjust stock, review requests, and manage registered users.",
                    color = TextGrey, fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onProductCatalog,
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Product Catalog", color = Navy, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onReviewRequests,
                    colors = ButtonDefaults.buttonColors(containerColor = Amber),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Review Requests", color = Navy, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = onUsersList,
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Registered Users", color = Navy)
                }
            }
        }
    }
}
