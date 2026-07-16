package edu.cit.Macopia.StockManagement_System.features.products.getproduct;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductView;
import edu.cit.Macopia.StockManagement_System.common.entity.Product;
import edu.cit.Macopia.StockManagement_System.common.exception.ResourceNotFoundException;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProductService {

    private final ProductRepository productRepository;

    public ProductView getProduct(String sku) {
        Product product = productRepository.findById(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU " + sku));

        return new ProductView(
                product.getSku(),
                product.getProductName(),
                product.getCategory(),
                product.getDescription(),
                product.getQuantity(),
                product.getMinThreshold(),
                product.isLowStock()
        );
    }
}
