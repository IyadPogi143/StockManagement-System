package edu.cit.Macopia.StockManagement_System.features.products.deleteproduct;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class DeleteProductController {

    private final DeleteProductService deleteProductService;

    @DeleteMapping("/{sku}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String sku) {
        deleteProductService.deleteProduct(sku);
        return ResponseEntity.noContent().build();
    }
}
