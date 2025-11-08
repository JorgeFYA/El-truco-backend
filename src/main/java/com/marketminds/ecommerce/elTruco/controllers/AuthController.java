package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.dtos.AuthRequest;
import com.marketminds.ecommerce.elTruco.dtos.AuthResponse;
import com.marketminds.ecommerce.elTruco.models.Users;
import com.marketminds.ecommerce.elTruco.services.JwtService;
import com.marketminds.ecommerce.elTruco.services.UsersServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
@AllArgsConstructor
public class AuthController {
    // inyectar el servicio, inyección de dependencias (por constructor con lombok)
    private final UsersServiceImpl usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping(path = "/register") //http://localhost:8080/api/auth/register
    public Users registeredUser(@RequestBody Users user) {
        return usersService.addUser(user);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            // 1. Autenticar usando el email y el password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            // 2. Si es exitoso, cargar user details
            final UserDetails userDetails = usersService.loadUserByUsername(request.email());

            //3. Generar el token JWT
            final String jwt = jwtService.generateToken(userDetails);

            // 4. Devolver el token
            return  ResponseEntity.ok(new AuthResponse(jwt));

        } catch (BadCredentialsException e) {
            // Si las credenciales son incorrectas
            return ResponseEntity.status(401).body(new AuthResponse("Email o contraseña incorrectos"));
        }
    }
}
