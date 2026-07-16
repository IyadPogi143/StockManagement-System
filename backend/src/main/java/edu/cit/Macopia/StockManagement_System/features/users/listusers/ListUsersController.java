package edu.cit.Macopia.StockManagement_System.features.users.listusers;

import edu.cit.Macopia.StockManagement_System.common.dto.UserSummaryView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origin:http://localhost:5173}")
public class ListUsersController {

    private final ListUsersService listUsersService;

    // KNOWN LIMITATION (documented, not fixed per current scope): no server-side
    // authorization check yet. The AdminDashboard.jsx role check on the frontend
    // is a UX gate only, not real security.
    @GetMapping
    public ResponseEntity<List<UserSummaryView>> getAllUsers() {
        return ResponseEntity.ok(listUsersService.listUsers());
    }
}
