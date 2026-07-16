package edu.cit.Macopia.StockManagement_System.features.users.listusers;

import edu.cit.Macopia.StockManagement_System.common.dto.UserSummaryView;
import edu.cit.Macopia.StockManagement_System.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsersService {

    private final UserRepository userRepository;

    // Read-only. No create/update/promote logic exists in this slice on purpose —
    // ADMINISTRATOR accounts are only ever created via a direct SQL insert.
    public List<UserSummaryView> listUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserSummaryView(
                        user.getUserId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getMiddleName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().name(),
                        user.getDateCreated()
                ))
                .toList();
    }
}
