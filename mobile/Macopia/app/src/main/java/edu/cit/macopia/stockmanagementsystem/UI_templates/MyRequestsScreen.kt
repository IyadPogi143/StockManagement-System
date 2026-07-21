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
import edu.cit.macopia.stockmanagementsystem.network.ProductChangeRequestResponse

private fun statusColor(status: String): Color = when (status) {
    "APPROVED" -> Color(0xFF059669)
    "REJECTED" -> Color(0xFFDC2626)
    else -> Color(0xFFB45309)
}

private fun statusBg(status: String): Color = when (status) {
    "APPROVED" -> Color(0xFFE6EDE8)
    "REJECTED" -> Color(0xFFF5E6E3)
    else -> Color(0xFFF6ECDB)
}

@Composable
fun MyRequestsScreen(
    viewModel: ProductRequestViewModel,
    userId: Long,
    onBack: () -> Unit,
    onNewRequest: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadMyRequests(userId)
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
            Text("My Requests", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            TextButton(onClick = onNewRequest) {
                Text("New", color = Amber)
            }
        }

        if (viewModel.isLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Amber)
            }
        } else if (viewModel.myRequests.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("You haven't submitted any requests yet.", color = TextGrey, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.myRequests.value) { request ->
                    RequestCard(request)
                }
            }
        }
    }
}

@Composable
private fun RequestCard(request: ProductChangeRequestResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${request.requestType} — ${request.sku}", fontWeight = FontWeight.Bold, color = Navy, fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(statusBg(request.status))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(request.status, color = statusColor(request.status), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(request.createdAt.take(16).replace("T", " "), color = TextGrey, fontSize = 12.sp)

        if (!request.adminFeedback.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Admin feedback:", fontWeight = FontWeight.SemiBold, color = Navy, fontSize = 12.sp)
            Text(request.adminFeedback, color = TextGrey, fontSize = 13.sp)
        } else {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Awaiting review", color = TextGrey, fontSize = 12.sp)
        }
    }
}
