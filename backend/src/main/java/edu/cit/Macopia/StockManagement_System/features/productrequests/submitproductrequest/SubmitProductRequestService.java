package edu.cit.Macopia.StockManagement_System.features.productrequests.submitproductrequest;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductChangeRequestView;
import edu.cit.Macopia.StockManagement_System.common.entity.ProductChangeRequest;
import edu.cit.Macopia.StockManagement_System.common.entity.RequestStatus;
import edu.cit.Macopia.StockManagement_System.common.entity.RequestType;
import edu.cit.Macopia.StockManagement_System.common.entity.User;
import edu.cit.Macopia.StockManagement_System.common.exception.ResourceNotFoundException;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductChangeRequestRepository;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductRepository;
import edu.cit.Macopia.StockManagement_System.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmitProductRequestService {

    private final ProductChangeRequestRepository productChangeRequestRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductChangeRequestView submitRequest(SubmitProductRequestRequest request) {
        RequestType type = parseRequestType(request.getRequestType());

        User requestedBy = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getUserId()));

        boolean productExists = productRepository.existsBySku(request.getSku());

        switch (type) {
            case CREATE -> {
                if (productExists) {
                    throw new IllegalArgumentException("A product with SKU '" + request.getSku() + "' already exists");
                }
                requireField(request.getProductName(), "Product name is required for a CREATE request");
                requireField(request.getCategory(), "Category is required for a CREATE request");
                requireNonNegative(request.getQuantity(), "Quantity is required for a CREATE request");
                requireNonNegative(request.getMinThreshold(), "Minimum threshold is required for a CREATE request");
            }
            case UPDATE -> {
                if (!productExists) {
                    throw new ResourceNotFoundException("Product not found with SKU " + request.getSku());
                }
                requireField(request.getProductName(), "Product name is required for an UPDATE request");
                requireField(request.getCategory(), "Category is required for an UPDATE request");
                requireNonNegative(request.getQuantity(), "Quantity is required for an UPDATE request");
                requireNonNegative(request.getMinThreshold(), "Minimum threshold is required for an UPDATE request");
            }
            case DELETE -> {
                if (!productExists) {
                    throw new ResourceNotFoundException("Product not found with SKU " + request.getSku());
                }
                // Only sku (and optionally a reason in description) is needed for DELETE.
            }
        }

        ProductChangeRequest entity = new ProductChangeRequest(
                null,
                type,
                request.getSku(),
                request.getProductName(),
                request.getCategory(),
                request.getDescription(),
                request.getQuantity(),
                request.getMinThreshold(),
                requestedBy,
                RequestStatus.PENDING,
                null,
                null,
                java.time.LocalDateTime.now(),
                null
        );

        ProductChangeRequest saved = productChangeRequestRepository.save(entity);
        return toView(saved);
    }

    private RequestType parseRequestType(String raw) {
        try {
            return RequestType.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("requestType must be CREATE, UPDATE, or DELETE");
        }
    }

    private void requireField(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireNonNegative(Integer value, String message) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private ProductChangeRequestView toView(ProductChangeRequest r) {
        return new ProductChangeRequestView(
                r.getRequestId(),
                r.getRequestType().name(),
                r.getSku(),
                r.getProposedProductName(),
                r.getProposedCategory(),
                r.getProposedDescription(),
                r.getProposedQuantity(),
                r.getProposedMinThreshold(),
                r.getRequestedBy().getUsername(),
                r.getStatus().name(),
                r.getAdminFeedback(),
                r.getReviewedBy() != null ? r.getReviewedBy().getUsername() : null,
                r.getCreatedAt(),
                r.getReviewedAt()
        );
    }
}
