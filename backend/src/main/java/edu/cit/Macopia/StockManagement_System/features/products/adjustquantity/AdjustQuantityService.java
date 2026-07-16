package edu.cit.Macopia.StockManagement_System.features.products.adjustquantity;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductView;
import edu.cit.Macopia.StockManagement_System.common.entity.Product;
import edu.cit.Macopia.StockManagement_System.common.entity.StockTransaction;
import edu.cit.Macopia.StockManagement_System.common.entity.User;
import edu.cit.Macopia.StockManagement_System.common.exception.ResourceNotFoundException;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductRepository;
import edu.cit.Macopia.StockManagement_System.common.repository.StockTransactionRepository;
import edu.cit.Macopia.StockManagement_System.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdjustQuantityService {

    private final ProductRepository productRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final UserRepository userRepository;

    public ProductView adjustQuantity(String sku, AdjustQuantityRequest request) {
        if (request.getChangeAmount() == 0) {
            throw new IllegalArgumentException("Change amount cannot be zero");
        }

        Product product = productRepository.findById(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU " + sku));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));

        int newQuantity = product.getQuantity() + request.getChangeAmount();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Resulting quantity cannot be negative");
        }

        product.setQuantity(newQuantity);
        Product saved = productRepository.save(product);

        StockTransaction transaction = new StockTransaction(
                null,
                saved,
                user,
                request.getChangeAmount(),
                newQuantity,
                LocalDateTime.now()
        );
        stockTransactionRepository.save(transaction);

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
