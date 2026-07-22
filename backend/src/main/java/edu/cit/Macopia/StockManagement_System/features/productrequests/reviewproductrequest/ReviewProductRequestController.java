package edu.cit.Macopia.StockManagement_System.features.productrequests.reviewproductrequest;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductChangeRequestView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class ReviewProductRequestController {

    private final ReviewProductRequestService reviewProductRequestService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PatchMapping("/{requestId}/review")
    public ResponseEntity<ProductChangeRequestView> review(
            @PathVariable Long requestId,
            @Valid @RequestBody ReviewProductRequestRequest request
    ) {
        return ResponseEntity.ok(reviewProductRequestService.review(requestId, request));
    }
}
