package com.marketminds.ecommerce.elTruco.services;

import com.marketminds.ecommerce.elTruco.models.Users;

import java.util.List;

public interface UsersService {

    // firma del metodo
    public List<Users> getAllUsers();

    // firma del metodo
    Users getUserById(Long id);
    Users addUser(Users user);
    Users deleteUserById(Long id);
    Users updateUserById(Long id, Users updatedUser);

    boolean validateUser(Users user);
}
