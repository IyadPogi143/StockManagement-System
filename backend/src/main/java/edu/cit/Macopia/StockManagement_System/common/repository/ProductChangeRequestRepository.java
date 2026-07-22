package edu.cit.Macopia.StockManagement_System.common.repository;

import edu.cit.Macopia.StockManagement_System.common.entity.ProductChangeRequest;
import edu.cit.Macopia.StockManagement_System.common.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductChangeRequestRepository extends JpaRepository<ProductChangeRequest, Long> {
    List<ProductChangeRequest> findAllByOrderByCreatedAtDesc();
    List<ProductChangeRequest> findByStatusOrderByCreatedAtDesc(RequestStatus status);
    List<ProductChangeRequest> findByRequestedBy_UserIdOrderByCreatedAtDesc(Long userId);
}
