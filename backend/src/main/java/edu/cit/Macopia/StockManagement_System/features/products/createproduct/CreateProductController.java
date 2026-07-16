package edu.cit.Macopia.StockManagement_System.features.products.createproduct;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class CreateProductController {

    private final CreateProductService createProductService;

    @PostMapping
    public ResponseEntity<ProductView> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductView created = createProductService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
