package com.jiralite.config;

import com.jiralite.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Active Spring Security sur l'application
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Injection du filtre JWT via constructeur (bonne pratique)
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Désactive CSRF : inutile pour une API REST stateless (pas de session/cookie)
            .csrf(csrf -> csrf.disable())

            // 2. Politique de session STATELESS : Spring Security ne crée JAMAIS de session HTTP
            //    Chaque requête doit porter son propre token JWT
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 3. Règles d'autorisation des routes
            .authorizeHttpRequests(auth -> auth
                // Swagger UI (accessible sans authentification)
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                // Auth endpoints (login / register) : public
                .requestMatchers("/api/auth/**").permitAll()

                // Toutes les autres routes nécessitent un JWT valide
                .anyRequest().authenticated()
            )

            // 4. Ajoute le filtre JWT AVANT le filtre d'authentification standard de Spring
            //    Ainsi, chaque requête passe d'abord par JwtAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean PasswordEncoder : BCrypt avec force 10 (par défaut)
    // Utilisé dans UserService pour encoder les mots de passe
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean AuthenticationManager : nécessaire pour authentifier user/password
    // Utilisé dans le controller /api/auth/login
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
