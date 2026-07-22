package edu.cit.Macopia.StockManagement_System.features.productrequests.reviewproductrequest;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductChangeRequestView;
import edu.cit.Macopia.StockManagement_System.common.entity.ProductChangeRequest;
import edu.cit.Macopia.StockManagement_System.common.entity.RequestStatus;
import edu.cit.Macopia.StockManagement_System.common.entity.User;
import edu.cit.Macopia.StockManagement_System.common.exception.ResourceNotFoundException;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductChangeRequestRepository;
import edu.cit.Macopia.StockManagement_System.common.repository.UserRepository;
import edu.cit.Macopia.StockManagement_System.features.products.createproduct.CreateProductRequest;
import edu.cit.Macopia.StockManagement_System.features.products.createproduct.CreateProductService;
import edu.cit.Macopia.StockManagement_System.features.products.deleteproduct.DeleteProductService;
import edu.cit.Macopia.StockManagement_System.features.products.updateproduct.UpdateProductRequest;
import edu.cit.Macopia.StockManagement_System.features.products.updateproduct.UpdateProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewProductRequestService {

    private final ProductChangeRequestRepository productChangeRequestRepository;
    private final UserRepository userRepository;

    // Reuses the existing single-purpose slices instead of duplicating their
    // create/update/delete logic here. This slice's own job is strictly the
    // review workflow: validate the decision, apply the underlying change,
    // and record who reviewed it and why.
    private final CreateProductService createProductService;
    private final UpdateProductService updateProductService;
    private final DeleteProductService deleteProductService;

    // @Transactional: if applying the underlying product change fails (e.g. a
    // duplicate SKU appeared between submission and review), the request's
    // status must NOT be saved as approved either. Both succeed or both roll back.
    @Transactional
    public ProductChangeRequestView review(Long requestId, ReviewProductRequestRequest request) {
        ProductChangeRequest changeRequest = productChangeRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with id " + requestId));

        if (changeRequest.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("This request has already been reviewed");
        }

        User reviewer = userRepository.findById(request.getReviewedByUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + request.getReviewedByUserId()));

        String decision = request.getDecision().toUpperCase();
        if (!decision.equals("APPROVE") && !decision.equals("REJECT")) {
            throw new IllegalArgumentException("decision must be APPROVE or REJECT");
        }

        if (decision.equals("APPROVE")) {
            applyChange(changeRequest);
            changeRequest.setStatus(RequestStatus.APPROVED);
        } else {
            changeRequest.setStatus(RequestStatus.REJECTED);
        }

        changeRequest.setAdminFeedback(request.getFeedback());
        changeRequest.setReviewedBy(reviewer);
        changeRequest.setReviewedAt(LocalDateTime.now());

        ProductChangeRequest saved = productChangeRequestRepository.save(changeRequest);
        return toView(saved);
    }

    private void applyChange(ProductChangeRequest changeRequest) {
        switch (changeRequest.getRequestType()) {
            case CREATE -> {
                CreateProductRequest req = new CreateProductRequest();
                req.setSku(changeRequest.getSku());
                req.setProductName(changeRequest.getProposedProductName());
                req.setCategory(changeRequest.getProposedCategory());
                req.setDescription(changeRequest.getProposedDescription());
                req.setQuantity(changeRequest.getProposedQuantity());
                req.setMinThreshold(changeRequest.getProposedMinThreshold());
                createProductService.createProduct(req);
            }
            case UPDATE -> {
                UpdateProductRequest req = new UpdateProductRequest();
                req.setProductName(changeRequest.getProposedProductName());
                req.setCategory(changeRequest.getProposedCategory());
                req.setDescription(changeRequest.getProposedDescription());
                req.setQuantity(changeRequest.getProposedQuantity());
                req.setMinThreshold(changeRequest.getProposedMinThreshold());
                updateProductService.updateProduct(changeRequest.getSku(), req);
            }
            case DELETE -> deleteProductService.deleteProduct(changeRequest.getSku());
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
