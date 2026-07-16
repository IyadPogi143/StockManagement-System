package edu.cit.Macopia.StockManagement_System.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_change_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType requestType;

    // Proposed SKU for CREATE, target SKU for UPDATE/DELETE
    @Column(nullable = false)
    private String sku;

    // Proposed field values. Null for fields that don't apply to the request type
    // (e.g. all of these are null for a DELETE request except sku).
    @Column(nullable = true)
    private String proposedProductName;

    @Column(nullable = true)
    private String proposedCategory;

    @Column(nullable = true)
    private String proposedDescription; // also doubles as "reason" text for DELETE requests

    @Column(nullable = true)
    private Integer proposedQuantity;

    @Column(nullable = true)
    private Integer proposedMinThreshold;

    @ManyToOne
    @JoinColumn(name = "requested_by_user_id", nullable = false)
    private User requestedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(nullable = true)
    private String adminFeedback;

    @ManyToOne
    @JoinColumn(name = "reviewed_by_user_id", nullable = true)
    private User reviewedBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = true)
    private LocalDateTime reviewedAt;
}
