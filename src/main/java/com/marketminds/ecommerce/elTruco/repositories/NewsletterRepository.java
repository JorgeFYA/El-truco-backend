package com.marketminds.ecommerce.elTruco.repositories;

import com.marketminds.ecommerce.elTruco.models.Newsletter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NewsletterRepository extends JpaRepository<Newsletter, Long> {

    Optional<Newsletter> findByEmail(String email);

    @Query("SELECT r FROM Newsletter r WHERE r.active = true")
    List<Newsletter> findAllActive();
}
