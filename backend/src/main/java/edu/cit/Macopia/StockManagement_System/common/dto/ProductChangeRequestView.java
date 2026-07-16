package edu.cit.Macopia.StockManagement_System.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProductChangeRequestView {

    private Long requestId;
    private String requestType;
    private String sku;
    private String proposedProductName;
    private String proposedCategory;
    private String proposedDescription;
    private Integer proposedQuantity;
    private Integer proposedMinThreshold;
    private String requestedByUsername;
    private String status;
    private String adminFeedback;
    private String reviewedByUsername; // null until reviewed
    private LocalDateTime createdAt;
    private LocalDateTime reviewedAt; // null until reviewed
}
