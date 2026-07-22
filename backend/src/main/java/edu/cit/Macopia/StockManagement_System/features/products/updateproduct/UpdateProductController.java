package edu.cit.Macopia.StockManagement_System.features.products.updateproduct;

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
public class UpdateProductController {

    private final UpdateProductService updateProductService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/{sku}")
    public ResponseEntity<ProductView> updateProduct(
            @PathVariable String sku,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return ResponseEntity.ok(updateProductService.updateProduct(sku, request));
    }
}
