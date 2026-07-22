package edu.cit.Macopia.StockManagement_System.features.auth.register;

import edu.cit.Macopia.StockManagement_System.common.entity.Role;
import edu.cit.Macopia.StockManagement_System.common.entity.User;
import edu.cit.Macopia.StockManagement_System.common.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest request) {
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
        user.setRole(Role.INVENTORY_CLERK); // self-registration always defaults to clerk

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getUserId(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getRole().name(),
                "Registration successful"
        );
    }
}
