package fr.asf.heatops.service;

import fr.asf.heatops.domain.entity.Company;
import fr.asf.heatops.domain.entity.User;
import fr.asf.heatops.repository.CompanyRepository;
import fr.asf.heatops.repository.UserRepository;
import fr.asf.heatops.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(String companyName, String email, String password) {

        // 1. Vérifier si la company existe déjà
        Company company = companyRepository.findByNameIgnoreCase(companyName)
                .orElseGet(() -> companyRepository.save(
                        Company.builder()
                                .name(companyName)
                                .createdAt(Instant.now())
                                .build()
                ));

        // 2. Vérifier si l’email existe déjà
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new RuntimeException("Email already registered");
        });

        // 3. Créer le user dans la company existante
        User user = userRepository.save(
                User.builder()
                        .company(company)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role("ADMIN")
                        .createdAt(Instant.now())
                        .build()
        );

        // 4. Générer le token avec le bon tenant
        return jwtService.generateToken(user.getEmail(), company.getId());
    }

    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtService.generateToken(email, user.getCompany().getId());
    }
}
