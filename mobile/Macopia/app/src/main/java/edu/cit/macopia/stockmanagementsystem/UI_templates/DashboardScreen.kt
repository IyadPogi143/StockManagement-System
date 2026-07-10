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
fun DashboardScreen(viewModel: AuthViewModel, onLogout: () -> Unit) {
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
        }
    }
}