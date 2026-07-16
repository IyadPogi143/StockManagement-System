package edu.cit.Macopia.StockManagement_System.features.productrequests.reviewproductrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewProductRequestRequest {

    @NotBlank(message = "Decision is required")
    private String decision; // "APPROVE" or "REJECT"

    @NotBlank(message = "Feedback is required")
    private String feedback;

    @NotNull(message = "reviewedByUserId is required")
    private Long reviewedByUserId;
}
