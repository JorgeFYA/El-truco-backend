package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.models.Users;
import com.marketminds.ecommerce.elTruco.services.UsersServiceImpl;
import lombok.AllArgsConstructor;
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

    @PostMapping(path = "/register") //http://localhost:8080/api/auth/register
    public Users registeredUser(@RequestBody Users user) {
        return usersService.addUser(user);
    }

    @PostMapping(path = "/login")
    public boolean loginUser(@RequestBody Users user) {
        return usersService.validateUser(user);
    }
}
