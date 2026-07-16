package edu.cit.Macopia.StockManagement_System.features.products.viewstockhistory;

import edu.cit.Macopia.StockManagement_System.common.dto.StockTransactionView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class ViewStockHistoryController {

    private final ViewStockHistoryService viewStockHistoryService;

    @GetMapping("/{sku}/transactions")
    public ResponseEntity<List<StockTransactionView>> getTransactionHistory(@PathVariable String sku) {
        return ResponseEntity.ok(viewStockHistoryService.getHistoryForProduct(sku));
    }
}
