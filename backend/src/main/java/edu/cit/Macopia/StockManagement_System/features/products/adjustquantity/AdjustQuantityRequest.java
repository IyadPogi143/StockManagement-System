package edu.cit.Macopia.StockManagement_System.features.products.adjustquantity;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustQuantityRequest {

    // Positive to increment, negative to decrement. Zero is rejected in the service layer.
    @NotNull(message = "Change amount is required")
    private Integer changeAmount;

    // Which user performed this adjustment, used for the StockTransaction log.
    @NotNull(message = "userId is required")
    private Long userId;
}
