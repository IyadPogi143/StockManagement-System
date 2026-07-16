package edu.cit.Macopia.StockManagement_System.features.productrequests.listmyproductrequests;

import edu.cit.Macopia.StockManagement_System.common.dto.ProductChangeRequestView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-requests/mine")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class ListMyProductRequestsController {

    private final ListMyProductRequestsService listMyProductRequestsService;

    @GetMapping
    public ResponseEntity<List<ProductChangeRequestView>> listMyRequests(@RequestParam Long userId) {
        return ResponseEntity.ok(listMyProductRequestsService.listMyRequests(userId));
    }
}
