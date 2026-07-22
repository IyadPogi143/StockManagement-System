package edu.cit.Macopia.StockManagement_System.features.auth.login;

import edu.cit.Macopia.StockManagement_System.common.entity.User;
import edu.cit.Macopia.StockManagement_System.common.repository.UserRepository;
import edu.cit.Macopia.StockManagement_System.common.security.JwtService;
import edu.cit.Macopia.StockManagement_System.common.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        UserPrincipal principal = new UserPrincipal(user);
        String accessToken = jwtService.generateAccessToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);

        return new LoginResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                "Login successful",
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpirationMs()
        );
    }
}
