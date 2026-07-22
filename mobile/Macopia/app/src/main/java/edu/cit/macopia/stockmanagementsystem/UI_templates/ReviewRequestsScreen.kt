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

@Composable
fun ReviewRequestsScreen(
    viewModel: ProductRequestViewModel,
    reviewerUserId: Long,
    onBack: () -> Unit
) {
    var activeRequest by remember { mutableStateOf<ProductChangeRequestResponse?>(null) }
    var feedbackText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadQueue("PENDING")
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
            Text("Review Requests", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(48.dp))
        }

        if (viewModel.isLoading.value && viewModel.queueRequests.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Amber)
            }
        } else if (viewModel.queueRequests.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("No pending requests.", color = TextGrey, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.queueRequests.value) { request ->
                    QueueCard(request) {
                        activeRequest = request
                        feedbackText = ""
                    }
                }
            }
        }
    }

    activeRequest?.let { request ->
        AlertDialog(
            onDismissRequest = { activeRequest = null },
            title = { Text("Review Request", color = Navy, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("${request.requestType} — ${request.sku}", fontWeight = FontWeight.SemiBold, color = Navy)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(describeChange(request), color = TextGrey, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Requested by: ${request.requestedByUsername}", color = TextGrey, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        label = { Text("Feedback (required)") },
                        modifier = Modifier.fillMaxWidth().height(90.dp)
                    )
                    viewModel.errorMessage.value?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (feedbackText.isNotBlank()) {
                            viewModel.review(request.requestId, "APPROVE", feedbackText, reviewerUserId) {
                                activeRequest = null
                                viewModel.loadQueue("PENDING")
                            }
                        }
                    }
                ) {
                    Text("Approve", color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                Row {
                    TextButton(
                        onClick = {
                            if (feedbackText.isNotBlank()) {
                                viewModel.review(request.requestId, "REJECT", feedbackText, reviewerUserId) {
                                    activeRequest = null
                                    viewModel.loadQueue("PENDING")
                                }
                            }
                        }
                    ) {
                        Text("Reject", color = Color(0xFFDC2626))
                    }
                    TextButton(onClick = { activeRequest = null }) {
                        Text("Cancel", color = TextGrey)
                    }
                }
            }
        )
    }
}

private fun describeChange(r: ProductChangeRequestResponse): String = when (r.requestType) {
    "CREATE" -> "Add \"${r.proposedProductName}\" (${r.proposedCategory}), qty ${r.proposedQuantity}, min ${r.proposedMinThreshold}"
    "UPDATE" -> "Update to \"${r.proposedProductName}\" (${r.proposedCategory}), qty ${r.proposedQuantity}, min ${r.proposedMinThreshold}"
    else -> "Delete this product" + if (!r.proposedDescription.isNullOrBlank()) " — reason: ${r.proposedDescription}" else ""
}

@Composable
private fun QueueCard(request: ProductChangeRequestResponse, onReview: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("${request.requestType} — ${request.sku}", fontWeight = FontWeight.Bold, color = Navy, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(describeChange(request), color = TextGrey, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("By ${request.requestedByUsername}", color = TextGrey, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = onReview,
            colors = ButtonDefaults.buttonColors(containerColor = Amber),
            modifier = Modifier.height(38.dp)
        ) {
            Text("Review", color = Navy, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}
