    package com.jiralite.service;

    import com.jiralite.dto.UserRegister;
    import com.jiralite.dto.UserResponse;
    import com.jiralite.enums.Role;
    import com.jiralite.exception.ConflictException;
    import com.jiralite.model.User;
    import com.jiralite.repository.UserRepository;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.time.format.DateTimeFormatter;

    @Service
    public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }

        public UserResponse register(UserRegister request) {
            // 1. Vérifier si l'email est déjà utilisé
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ConflictException("Email already in use");
            }

            // 2. Vérifier si le username est déjà utilisé
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new ConflictException("Username already in use");
            }

            // 3. Créer l'utilisateur avec le mot de passe hashé
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.DEVELOPER);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            User saved = userRepository.save(user);

            // 4. Retourner la réponse sans le mot de passe
            return new UserResponse(
                    saved.getId(),
                    saved.getUsername(),
                    saved.getEmail(),
                    saved.getRole().name(),
                    saved.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME)
            );
        }
    }
