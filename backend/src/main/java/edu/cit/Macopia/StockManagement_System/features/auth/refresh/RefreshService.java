package edu.cit.Macopia.StockManagement_System.features.auth.refresh;

import edu.cit.Macopia.StockManagement_System.common.entity.User;
import edu.cit.Macopia.StockManagement_System.common.repository.UserRepository;
import edu.cit.Macopia.StockManagement_System.common.security.JwtService;
import edu.cit.Macopia.StockManagement_System.common.security.UserPrincipal;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshService {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public RefreshResponse refresh(RefreshRequest request) {
        String token = request.getRefreshToken();

        String username;
        try {
            String tokenType = jwtService.extractTokenType(token);
            if (!"refresh".equals(tokenType)) {
                throw new IllegalArgumentException("The provided token is not a refresh token");
            }
            username = jwtService.extractUsername(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Refresh token is invalid or expired. Please log in again.");
        }

        if (!jwtService.isTokenValid(token, username)) {
            throw new IllegalArgumentException("Refresh token is invalid or expired. Please log in again.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token is invalid or expired. Please log in again."));

        UserPrincipal principal = new UserPrincipal(user);
        // Rotate both tokens on every refresh rather than reusing the same
        // refresh token repeatedly, this limits how long a leaked refresh
        // token stays useful.
        String newAccessToken = jwtService.generateAccessToken(principal);
        String newRefreshToken = jwtService.generateRefreshToken(principal);

        return new RefreshResponse(newAccessToken, newRefreshToken, jwtService.getAccessTokenExpirationMs());
    }
}
