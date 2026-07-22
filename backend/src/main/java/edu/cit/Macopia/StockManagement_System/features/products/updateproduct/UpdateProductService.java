package edu.cit.Macopia.StockManagement_System.features.products.updateproduct;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductView;
import edu.cit.Macopia.StockManagement_System.common.entity.Product;
import edu.cit.Macopia.StockManagement_System.common.exception.ResourceNotFoundException;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductService {

    private final ProductRepository productRepository;

    public ProductView updateProduct(String sku, UpdateProductRequest request) {
        Product product = productRepository.findById(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU " + sku));

        // SKU itself is never changed on update; the path variable is authoritative.
        product.setProductName(request.getProductName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());
        product.setMinThreshold(request.getMinThreshold());
        Product saved = productRepository.save(product);

        return new ProductView(
                saved.getSku(),
                saved.getProductName(),
                saved.getCategory(),
                saved.getDescription(),
                saved.getQuantity(),
                saved.getMinThreshold(),
                saved.isLowStock()
        );
    }
}
