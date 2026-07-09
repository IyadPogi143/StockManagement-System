package edu.cit.Macopia.StockManagement_System.service;

import edu.cit.Macopia.StockManagement_System.dto.AuthLoginRequest;
import edu.cit.Macopia.StockManagement_System.dto.AuthRegisterRequest;
import edu.cit.Macopia.StockManagement_System.dto.AuthResponse;
import edu.cit.Macopia.StockManagement_System.entity.Role;
import edu.cit.Macopia.StockManagement_System.entity.User;
import edu.cit.Macopia.StockManagement_System.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(AuthRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMiddleName(request.getMiddleName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.INVENTORY_CLERK); // default role on self-registration

        User saved = userRepository.save(user);

        return new AuthResponse(
                saved.getUserId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getRole().name(),
                "Registration successful"
        );
    }

    public AuthResponse login(AuthLoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return new AuthResponse(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                "Login successful"
        );
    }
}