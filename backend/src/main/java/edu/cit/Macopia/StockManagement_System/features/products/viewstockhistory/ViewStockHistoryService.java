package edu.cit.Macopia.StockManagement_System.features.products.viewstockhistory;

import edu.cit.Macopia.StockManagement_System.common.dto.StockTransactionView;
import edu.cit.Macopia.StockManagement_System.common.repository.StockTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewStockHistoryService {

    private final StockTransactionRepository stockTransactionRepository;

    public List<StockTransactionView> getHistoryForProduct(String sku) {
        return stockTransactionRepository.findByProduct_SkuOrderByTimestampDesc(sku).stream()
                .map(tx -> new StockTransactionView(
                        tx.getTransactionId(),
                        tx.getProduct().getSku(),
                        tx.getProduct().getProductName(),
                        tx.getUser().getUsername(),
                        tx.getChangeAmount(),
                        tx.getResultingQuantity(),
                        tx.getTimestamp()
                ))
                .toList();
    }
}
