package edu.cit.Macopia.StockManagement_System.features.products.listproducts;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductView;
import edu.cit.Macopia.StockManagement_System.common.entity.Product;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListProductsService {

    private final ProductRepository productRepository;

    public List<ProductView> listProducts() {
        return productRepository.findAll().stream()
                .map(this::toView)
                .toList();
    }

    private ProductView toView(Product product) {
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
