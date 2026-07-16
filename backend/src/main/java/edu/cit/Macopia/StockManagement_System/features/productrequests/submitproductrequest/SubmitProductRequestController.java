package edu.cit.Macopia.StockManagement_System.features.productrequests.submitproductrequest;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductChangeRequestView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class SubmitProductRequestController {

    private final SubmitProductRequestService submitProductRequestService;

    // Reachable by both roles. Every add/edit/delete of a product goes through
    // here first and sits as PENDING until an Administrator reviews it.
    @PostMapping
    public ResponseEntity<ProductChangeRequestView> submitRequest(@Valid @RequestBody SubmitProductRequestRequest request) {
        ProductChangeRequestView created = submitProductRequestService.submitRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
