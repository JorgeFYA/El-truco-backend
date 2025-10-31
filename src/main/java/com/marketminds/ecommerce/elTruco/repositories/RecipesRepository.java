package com.marketminds.ecommerce.elTruco.repositories;

import com.marketminds.ecommerce.elTruco.models.Ingredient;
import com.marketminds.ecommerce.elTruco.models.Recipe;
import com.marketminds.ecommerce.elTruco.models.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipesRepository extends JpaRepository<Recipe, Long> {
    // Hereda métodos CRUD como findAll(), findById(), save(), etc.

    // JPA automáticamente implementa esta consulta: SELECT * FROM recipes WHERE slug = ?
    Optional<Recipe> findBySlug(String slug);

    // Tare todas las recetas activas
    @Query("SELECT r FROM Recipe r WHERE r.active = true")
    List<Recipe> findAllActive();

    // Trae todos los ingredientes de una receta, activos y no activos
    @Query("SELECT i FROM Ingredient i WHERE i.recipe.id = :recipeId")
    List<Ingredient> findAllIngredientsByRecipeId(@Param("recipeId") Long recipeId);

    // Trae todos los pasos de una receta, activos y no activos
    @Query("SELECT s FROM Step s WHERE s.recipe.id = :recipeId ORDER BY s.stepOrder")
    List<Step> findAllStepsByRecipeId(@Param("recipeId") Long recipeId);
}
