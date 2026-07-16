package edu.cit.Macopia.StockManagement_System.features.productrequests.listproductrequests;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductChangeRequestView;
import edu.cit.Macopia.StockManagement_System.common.entity.ProductChangeRequest;
import edu.cit.Macopia.StockManagement_System.common.entity.RequestStatus;
import edu.cit.Macopia.StockManagement_System.common.repository.ProductChangeRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListProductRequestsService {

    private final ProductChangeRequestRepository productChangeRequestRepository;

    public List<ProductChangeRequestView> listRequests(String statusFilter) {
        List<ProductChangeRequest> requests;
        if (statusFilter == null || statusFilter.isBlank()) {
            requests = productChangeRequestRepository.findAllByOrderByCreatedAtDesc();
        } else {
            RequestStatus status = RequestStatus.valueOf(statusFilter.toUpperCase());
            requests = productChangeRequestRepository.findByStatusOrderByCreatedAtDesc(status);
        }
        return requests.stream().map(this::toView).toList();
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
