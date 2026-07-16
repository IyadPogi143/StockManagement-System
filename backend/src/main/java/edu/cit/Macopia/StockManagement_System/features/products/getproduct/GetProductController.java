package edu.cit.Macopia.StockManagement_System.features.products.getproduct;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class GetProductController {

    private final GetProductService getProductService;

    @GetMapping("/{sku}")
    public ResponseEntity<ProductView> getProduct(@PathVariable String sku) {
        return ResponseEntity.ok(getProductService.getProduct(sku));
    }
}
