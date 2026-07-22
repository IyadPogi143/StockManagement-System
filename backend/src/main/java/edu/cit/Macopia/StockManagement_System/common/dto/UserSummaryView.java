package edu.cit.Macopia.StockManagement_System.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Read-only projection of User for the ListUsers slice.
// Deliberately has no passwordHash field, and never will.
@Getter
@Setter
@AllArgsConstructor
public class UserSummaryView {

    private Long userId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String username;
    private String email;
    private String role;
    private LocalDateTime dateCreated;
}
