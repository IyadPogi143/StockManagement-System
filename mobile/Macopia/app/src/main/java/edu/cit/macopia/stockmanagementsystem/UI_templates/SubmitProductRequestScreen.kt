package edu.cit.macopia.stockmanagementsystem.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitProductRequestScreen(
    viewModel: ProductRequestViewModel,
    userId: Long,
    onBack: () -> Unit,
    onViewMyRequests: () -> Unit
) {
    val context = LocalContext.current

    var requestType by remember { mutableStateOf("CREATE") }
    var typeMenuExpanded by remember { mutableStateOf(false) }
    var sku by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var minThreshold by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.submitSuccess.value) {
        if (viewModel.submitSuccess.value) {
            Toast.makeText(context, "Request submitted. Check My Requests for status.", Toast.LENGTH_LONG).show()
            sku = ""; productName = ""; category = ""; description = ""; quantity = ""; minThreshold = ""
            viewModel.submitSuccess.value = false
        }
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
            Text("Submit Request", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            TextButton(onClick = onViewMyRequests) {
                Text("My Requests", color = Amber)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                "Adding, editing, or deleting a product requires Administrator approval.",
                color = TextGrey, fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            viewModel.errorMessage.value?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text("Request Type", fontWeight = FontWeight.SemiBold, color = Navy, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            ExposedDropdownMenuBox(
                expanded = typeMenuExpanded,
                onExpandedChange = { typeMenuExpanded = !typeMenuExpanded }
            ) {
                OutlinedTextField(
                    value = when (requestType) {
                        "CREATE" -> "Add new product"
                        "UPDATE" -> "Edit existing product"
                        else -> "Delete existing product"
                    },
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = typeMenuExpanded, onDismissRequest = { typeMenuExpanded = false }) {
                    DropdownMenuItem(text = { Text("Add new product") }, onClick = { requestType = "CREATE"; typeMenuExpanded = false })
                    DropdownMenuItem(text = { Text("Edit existing product") }, onClick = { requestType = "UPDATE"; typeMenuExpanded = false })
                    DropdownMenuItem(text = { Text("Delete existing product") }, onClick = { requestType = "DELETE"; typeMenuExpanded = false })
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text(if (requestType == "CREATE") "SKU" else "SKU (of the existing product)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (requestType != "DELETE") {
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it.filter { c -> c.isDigit() } },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = minThreshold,
                        onValueChange = { minThreshold = it.filter { c -> c.isDigit() } },
                        label = { Text("Min Threshold") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(if (requestType == "DELETE") "Reason for deletion" else "Description") },
                modifier = Modifier.fillMaxWidth().height(90.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.submitRequest(
                        requestType = requestType,
                        sku = sku,
                        productName = if (requestType != "DELETE") productName else null,
                        category = if (requestType != "DELETE") category else null,
                        description = description.ifBlank { null },
                        quantity = if (requestType != "DELETE") quantity.toIntOrNull() else null,
                        minThreshold = if (requestType != "DELETE") minThreshold.toIntOrNull() else null,
                        userId = userId
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Amber),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = !viewModel.isLoading.value && sku.isNotBlank()
            ) {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Navy)
                } else {
                    Text("Submit Request", color = Navy, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
