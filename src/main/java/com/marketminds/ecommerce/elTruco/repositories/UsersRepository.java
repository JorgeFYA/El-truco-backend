package com.marketminds.ecommerce.elTruco.repositories;

import com.marketminds.ecommerce.elTruco.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);
}