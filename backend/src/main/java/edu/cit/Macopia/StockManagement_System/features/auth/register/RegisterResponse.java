package edu.cit.Macopia.StockManagement_System.features.auth.register;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String role;
    private String message;
}
