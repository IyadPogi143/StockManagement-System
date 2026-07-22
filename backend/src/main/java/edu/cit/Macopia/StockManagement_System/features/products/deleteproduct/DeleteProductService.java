package edu.cit.Macopia.StockManagement_System.features.products.deleteproduct;

import edu.cit.Macopia.StockManagement_System.common.entity.Product;
import edu.cit.Macopia.StockManagement_System.common.exception.ResourceNotFoundException;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteProductService {

    private final ProductRepository productRepository;

    public void deleteProduct(String sku) {
        Product product = productRepository.findById(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU " + sku));
        productRepository.delete(product);
    }
}
