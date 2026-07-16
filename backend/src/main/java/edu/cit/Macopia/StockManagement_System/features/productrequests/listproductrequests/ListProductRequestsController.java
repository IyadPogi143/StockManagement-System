package edu.cit.Macopia.StockManagement_System.features.productrequests.listproductrequests;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductChangeRequestView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-requests")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class ListProductRequestsController {

    private final ListProductRequestsService listProductRequestsService;

    // Intended for Administrators (frontend-gated, same known limitation as
    // other endpoints: no server-side role check yet).
    @GetMapping
    public ResponseEntity<List<ProductChangeRequestView>> listRequests(
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(listProductRequestsService.listRequests(status));
    }
}
