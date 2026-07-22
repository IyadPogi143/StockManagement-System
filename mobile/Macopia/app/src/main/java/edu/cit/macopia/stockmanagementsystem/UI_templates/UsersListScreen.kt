package edu.cit.macopia.stockmanagementsystem.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.macopia.stockmanagementsystem.network.UserSummaryResponse

@Composable
fun UsersListScreen(
    viewModel: AdminViewModel,
    onBack: () -> Unit
) {
    var selectedUser by remember { mutableStateOf<UserSummaryResponse?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("Back", color = Amber)
            }
            Text("Registered Users", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(48.dp))
        }

        if (viewModel.isLoading.value && viewModel.users.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Amber)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.users.value) { user ->
                    UserCard(user) { selectedUser = user }
                }
            }
        }
    }

    selectedUser?.let { user ->
        AlertDialog(
            onDismissRequest = { selectedUser = null },
            title = { Text("User Profile", color = Navy, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    ProfileRow("Full Name", "${user.firstName} ${user.middleName ?: ""} ${user.lastName}".replace("  ", " "))
                    ProfileRow("Username", user.username)
                    ProfileRow("Email", user.email)
                    ProfileRow("Role", user.role.replace("_", " "))
                    ProfileRow("Date Registered", user.dateCreated.take(10))
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedUser = null }) {
                    Text("Close", color = Navy)
                }
            }
        )
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(label, color = TextGrey, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Text(value, color = Navy, fontSize = 14.sp)
    }
}

@Composable
private fun UserCard(user: UserSummaryResponse, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("${user.firstName} ${user.lastName}", fontWeight = FontWeight.Bold, color = Navy, fontSize = 15.sp)
            Text(user.username, color = TextGrey, fontSize = 12.sp)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (user.role == "ADMINISTRATOR") Color(0xFFF3E6D3) else Color(0xFFECEEF5))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    user.role.replace("_", " "),
                    color = if (user.role == "ADMINISTRATOR") AmberDark else Color(0xFF3D4A7A),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            TextButton(onClick = onClick) {
                Text("View", color = Navy, fontSize = 13.sp)
            }
        }
    }
}
