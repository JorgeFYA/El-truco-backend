package com.marketminds.ecommerce.elTruco.services;

import com.marketminds.ecommerce.elTruco.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipesRepository extends JpaRepository<Recipe, Long> {
    // Hereda métodos CRUD como findAll(), findById(), save(), etc.

    // JPA automáticamente implementa esta consulta: SELECT * FROM recipes WHERE slug = ?
    Optional<Recipe> findBySlug(String slug);
}
