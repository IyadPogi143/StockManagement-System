package edu.cit.Macopia.StockManagement_System.features.products.deleteproduct;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class DeleteProductController {

    private final DeleteProductService deleteProductService;

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String sku) {
        deleteProductService.deleteProduct(sku);
        return ResponseEntity.noContent().build();
    }
}
