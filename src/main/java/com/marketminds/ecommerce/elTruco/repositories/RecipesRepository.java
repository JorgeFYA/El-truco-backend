package com.marketminds.ecommerce.elTruco.repositories;

import com.marketminds.ecommerce.elTruco.models.Ingredients;
import com.marketminds.ecommerce.elTruco.models.Recipes;
import com.marketminds.ecommerce.elTruco.models.Steps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipesRepository extends JpaRepository<Recipes, Long> {
    // Hereda métodos CRUD como findAll(), findById(), save(), etc.

    // JPA automáticamente implementa esta consulta: SELECT * FROM recipes WHERE slug = ?
    Optional<Recipes> findBySlug(String slug);

    // Trae todas las recetas activas
    @Query("SELECT r FROM Recipes r WHERE r.active = true")
    List<Recipes> findAllActive();

    // Trae todos los ingredientes de una receta, activos y no activos
    @Query("SELECT i FROM Ingredients i WHERE i.recipe.id = :recipeId")
    List<Ingredients> findAllIngredientsByRecipeId(@Param("recipeId") Long recipeId);

    // Trae todos los pasos de una receta, activos y no activos
    @Query("SELECT s FROM Steps s WHERE s.recipe.id = :recipeId ORDER BY s.stepOrder")
    List<Steps> findAllStepsByRecipeId(@Param("recipeId") Long recipeId);
}
