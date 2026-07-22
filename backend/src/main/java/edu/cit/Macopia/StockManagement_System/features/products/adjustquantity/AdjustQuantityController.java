package edu.cit.Macopia.StockManagement_System.features.products.adjustquantity;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class AdjustQuantityController {

    private final AdjustQuantityService adjustQuantityService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PatchMapping("/{sku}/quantity")
    public ResponseEntity<ProductView> adjustQuantity(
            @PathVariable String sku,
            @Valid @RequestBody AdjustQuantityRequest request
    ) {
        return ResponseEntity.ok(adjustQuantityService.adjustQuantity(sku, request));
    }
}
