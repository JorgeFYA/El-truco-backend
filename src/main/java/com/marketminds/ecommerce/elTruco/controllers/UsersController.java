package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.models.Users;
import com.marketminds.ecommerce.elTruco.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/users")
@AllArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping
    public List<Users> getAllUsers() {
        return usersService.getAllUsers();
    }

    @GetMapping(path = "/{userId}")
    public Users getUserById(@PathVariable("userId") Long id) {
        return usersService.getUserById(id);
    }

    @PutMapping(path = "/{userId}")
    public Users updateUserById(@PathVariable("userId") Long id, @RequestBody Users user) {
        return usersService.updateUserById(id, user);
    }

    @DeleteMapping(path = "/{userId}")
    public Users deleteUserById(@PathVariable("userId") Long id) {
        return usersService.deleteUserById(id);
    }
}
