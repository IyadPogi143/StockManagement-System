package edu.cit.Macopia.StockManagement_System.features.products.createproduct;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductView;
import edu.cit.Macopia.StockManagement_System.common.entity.Product;
import edu.cit.Macopia.StockManagement_System.common.exception.DuplicateResourceException;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProductService {

    private final ProductRepository productRepository;

    public ProductView createProduct(CreateProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("A product with SKU '" + request.getSku() + "' already exists");
        }

        Product product = new Product(
                request.getSku(),
                request.getProductName(),
                request.getCategory(),
                request.getDescription(),
                request.getQuantity(),
                request.getMinThreshold()
        );
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
