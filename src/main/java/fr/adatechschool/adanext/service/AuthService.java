package fr.adatechschool.adanext.service;

import fr.adatechschool.adanext.dto.request.RegisterRequest;
import fr.adatechschool.adanext.dto.response.UserResponse;
import fr.adatechschool.adanext.exception.EmailAlreadyUsedException;
import fr.adatechschool.adanext.model.User;
import fr.adatechschool.adanext.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyUsedException(request.getEmail());
        }

        User user = new User(
            request.getEmail(),
            passwordEncoder.encode(request.getPassword()),
            request.getName()
        );

        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }
}
