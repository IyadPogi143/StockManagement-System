package edu.cit.Macopia.StockManagement_System.features.productrequests.listmyproductrequests;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductChangeRequestView;
import edu.cit.Macopia.StockManagement_System.common.entity.ProductChangeRequest;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductChangeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListMyProductRequestsService {

    private final ProductChangeRequestRepository productChangeRequestRepository;

    public List<ProductChangeRequestView> listMyRequests(Long userId) {
        return productChangeRequestRepository.findByRequestedBy_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toView)
                .toList();
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
