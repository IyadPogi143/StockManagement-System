package edu.cit.Macopia.StockManagement_System.features.auth.refresh;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshResponse {
    private String accessToken;
    private String refreshToken;
    private long expiresInMs;
}
