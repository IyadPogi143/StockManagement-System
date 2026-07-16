package edu.cit.Macopia.StockManagement_System.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Read shape for the ViewStockHistory slice.
@Getter
@Setter
@AllArgsConstructor
public class StockTransactionView {

    private Long transactionId;
    private String sku;
    private String productName;
    private String username;
    private Integer changeAmount;
    private Integer resultingQuantity;
    private LocalDateTime timestamp;
}
