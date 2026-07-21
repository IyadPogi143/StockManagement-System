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
import edu.cit.macopia.stockmanagementsystem.network.ProductResponse

@Composable
fun ProductCatalogScreen(
    viewModel: AdminViewModel,
    userId: Long,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
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
            Text("Product Catalog", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(48.dp))
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "View-only. Adding, editing, or deleting products requires a request approved through Review Requests.",
                color = TextGrey, fontSize = 12.sp
            )
        }

        viewModel.errorMessage.value?.let {
            Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 16.dp))
        }

        if (viewModel.isLoading.value && viewModel.products.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Amber)
            }
        } else if (viewModel.products.value.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("No products yet.", color = TextGrey, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.products.value) { product ->
                    ProductCard(product) { change ->
                        viewModel.adjustQuantity(product.sku, change, userId)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(product: ProductResponse, onAdjust: (Int) -> Unit) {
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
            Column {
                Text(product.productName, fontWeight = FontWeight.Bold, color = Navy, fontSize = 15.sp)
                Text("${product.sku} • ${product.category}", color = TextGrey, fontSize = 12.sp)
            }
            if (product.lowStock) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF5E6E3))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("LOW STOCK", color = Color(0xFFDC2626), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE6EDE8))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("OK", color = Color(0xFF059669), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedButton(
                onClick = { onAdjust(-1) },
                enabled = product.quantity > 0,
                modifier = Modifier.size(36.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("-", fontWeight = FontWeight.Bold, color = Navy)
            }
            Text("${product.quantity}", fontWeight = FontWeight.Bold, color = Navy, fontSize = 16.sp)
            OutlinedButton(
                onClick = { onAdjust(1) },
                modifier = Modifier.size(36.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("+", fontWeight = FontWeight.Bold, color = Navy)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Min: ${product.minThreshold}", color = TextGrey, fontSize = 12.sp)
        }
    }
}
