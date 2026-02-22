package com.jiralite.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtre JWT exécuté UNE SEULE FOIS par requête HTTP (OncePerRequestFilter).
 * Il intercepte chaque requête, extrait le token JWT du header Authorization,
 * le valide, et si valide, authentifie l'utilisateur dans le SecurityContext.
 */
@Component // Spring le détecte automatiquement comme bean
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // Injection via constructeur (évite les problèmes de cycle de dépendance)
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,    // La requête HTTP entrante
            @NonNull HttpServletResponse response,  // La réponse HTTP sortante
            @NonNull FilterChain filterChain        // La chaîne de filtres suivants
    ) throws ServletException, IOException {

        // Étape 1 : Extraire le token JWT du header "Authorization: Bearer <token>"
        String token = extractTokenFromRequest(request);

        // Étape 2 : Si un token est présent ET valide, authentifier l'utilisateur
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // Extrait le username (email) du token
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // Extrait le rôle depuis les claims du token (ex: "ROLE_ADMIN")
            String role = (String) jwtTokenProvider.getClaimsFromToken(token).get("role");

            // Crée l'objet Authentication avec username + autorités (rôles)
            // UsernamePasswordAuthenticationToken(principal, credentials, authorities)
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    username,   // principal : identité de l'utilisateur
                    null,       // credentials : null car on ne stocke pas le mdp dans JWT
                    List.of(new SimpleGrantedAuthority("ROLE_" + role)) // autorités Spring Security
                );

            // Étape 3 : Enregistre l'authentification dans le SecurityContext
            // Spring Security vérifiera ce contexte pour chaque accès à une route protégée
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Étape 4 : Passe la requête au filtre suivant dans la chaîne
        // (que le token soit valide ou non — Spring Security bloquera si nécessaire)
        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token JWT du header Authorization.
     * Format attendu : "Authorization: Bearer eyJhbGciOi..."
     *
     * @return le token sans le préfixe "Bearer ", ou null si absent/malformé
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        // Récupère la valeur du header Authorization
        String bearerToken = request.getHeader("Authorization");

        // Vérifie que le header existe et commence par "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Retourne le token sans le préfixe "Bearer " (7 caractères)
            return bearerToken.substring(7);
        }

        // Pas de token trouvé
        return null;
    }
}

