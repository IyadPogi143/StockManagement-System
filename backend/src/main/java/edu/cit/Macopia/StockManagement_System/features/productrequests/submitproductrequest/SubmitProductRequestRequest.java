package edu.cit.Macopia.StockManagement_System.features.productrequests.submitproductrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitProductRequestRequest {

    @NotBlank(message = "Request type is required")
    private String requestType; // "CREATE", "UPDATE", or "DELETE"

    @NotBlank(message = "SKU is required")
    private String sku;

    // Only required for CREATE/UPDATE; validated conditionally in the service
    // since their necessity depends on requestType.
    private String productName;
    private String category;
    private String description; // also used as "reason" text for DELETE requests
    private Integer quantity;
    private Integer minThreshold;

    @NotNull(message = "userId is required")
    private Long userId;
}
