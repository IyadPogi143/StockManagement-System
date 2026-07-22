package edu.cit.Macopia.StockManagement_System.features.products.listproducts;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class ListProductsController {

    private final ListProductsService listProductsService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<ProductView>> getAllProducts() {
        return ResponseEntity.ok(listProductsService.listProducts());
    }
}
