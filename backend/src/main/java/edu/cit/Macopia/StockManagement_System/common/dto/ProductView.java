package edu.cit.Macopia.StockManagement_System.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Shared READ shape for a Product, used by any slice that returns product
// data (CreateProduct, ListProducts, GetProduct, UpdateProduct, AdjustQuantity).
// This is intentionally shared rather than duplicated per slice because it
// represents the same public view of the Product aggregate everywhere it's
// returned. Slice independence is preserved on the INPUT side instead: each
// slice still owns its own Request DTO and validation rules.
@Getter
@Setter
@AllArgsConstructor
public class ProductView {

    private String sku;
    private String productName;
    private String category;
    private String description;
    private Integer quantity;
    private Integer minThreshold;
    private boolean lowStock;
}
