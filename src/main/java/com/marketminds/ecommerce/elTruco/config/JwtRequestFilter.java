package com.marketminds.ecommerce.elTruco.config;

import com.marketminds.ecommerce.elTruco.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Validar que el header esté bien
        // Early return o cláusula de guarda:
        // buscamos primero el caso de fallo para interrumpir la ejecución lo antes posible.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer token del header
        // Ejemplo del header en Authorization:
        // Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
        jwt = authHeader.substring(7);

        try {
            // 3. Extraer username y rol del token
            username = jwtService.extractUsername(jwt);
            String role = jwtService.extractRole(jwt); // Nuevo: obtenemos el rol directamente del JWT

            // 4. Verificar username y que no haya autenticación previa
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. Validar el token (firma y expiración)
                if (jwtService.isValidToken(jwt, username)) {

                    // 6. Construir la lista de autoridades a partir del rol del JWT
                    var authorities = List.of(new SimpleGrantedAuthority(role));

                    // 7. Crear la autenticación y guardarla en el contexto de seguridad
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            // 8. Continuar con el siguiente filtro
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // Si el token no es válido, expiró o la firma no coincide
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
        }
    }
}
