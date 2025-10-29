package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.models.*;
import com.marketminds.ecommerce.elTruco.services.RecipesRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = {
        "http://127.0.0.1:5501",
        "https://jorgefya.github.io/el-truco/"
})
@RestController
@RequestMapping("api/recipes")
public class RecipesController {

    @Autowired
    private RecipesRepository repo;

    // Este metodo maneja las peticiones GET a: localhost:8080/recipes
    // GET: solo recetas activas
    @GetMapping({"", "/"})
    public List<Recipe> getRecipeList() {
        // Usa el repositorio para obtener todas las recetas activas de MySQL
        // Devuelve la lista. Spring Boot se encarga de convertir List<Recipe> a JSON.
        return repo.findAllActive();
    }

    // Endpoint para mostrar recetas individuales, localhost:8080/recipes/{slug}
    @GetMapping("/{slug}")
    public Recipe getRecipeBySlug(@PathVariable String slug) {

        // Llama al repositorio para buscar el Optional
        Recipe recipe = repo.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Receta no encontrada con slug: " + slug
                ));
        if (!recipe.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Receta no encontrada con slug: " + slug);
        }
        return recipe;
    }

    // Endpoint para obtener receta por ID (para admin)
    @GetMapping("/admin/{id}")
    public Recipe getRecipeByIdForAdmin(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Receta no encontrada con ID: " + id
                ));
    }

    // Este metodo maneja las peticiones POST a: localhost:8080/recipes
    // POST: crear o reactivar receta
    @PostMapping({"", "/"})
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        String slug = new Recipe().slugify(recipeDto.getName());

        Recipe recipe = repo.findBySlug(slug)
                .filter(r -> !r.isActive())
                .orElse(new Recipe());

        recipe.setName(recipeDto.getName());
        recipe.setShortDescription(recipeDto.getShortDescription());
        recipe.setFullDescription(recipeDto.getFullDescription());
        recipe.setCategory(recipeDto.getCategory());
        recipe.setActive(true);

        updateRecipeDetails(recipe, recipeDto);

        Recipe saved = repo.save(recipe);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Este metodo maneja las peticiones POST para subida/actualización de imagen, localhost:8080/recipes/{id}/upload-image
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Recipe> uploadImage(@PathVariable Long id, @RequestParam("image")MultipartFile file) {
        Recipe recipe = repo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Receta con ID '" + id + "' no encontrada"
        ));

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Archivo de imagen vacío");
        }

        String storageFilename;
        String uploadDir = "public/images/recipes/";

        try {
            String oldImageFilename = recipe.getImageFilename();

            if (oldImageFilename != null && !oldImageFilename.isEmpty()) {
                Path oldImagePath = Paths.get(uploadDir + oldImageFilename);

                try {
                    Files.deleteIfExists(oldImagePath);
                } catch (Exception e) {
                    System.err.println("Error al eliminar la imagen anterior: " + oldImagePath + " - " + e.getMessage());
                }
            }

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            storageFilename = new Date().getTime() + "-" + file.getOriginalFilename();

            try ( InputStream inputStream = file.getInputStream() ) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFilename), StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guadar el archivo de imagen: " + exception.getMessage());
        }

        recipe.setImageFilename(storageFilename);
        Recipe updateRecipe = repo.save(recipe);

        return ResponseEntity.ok(updateRecipe);
    }

    // Este metodo maneja las peticiones PUT a: localhost:8080/recipes
    // PUT: actualizar receta existente
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeDto recipeDto) {

        Recipe recipe = repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Receta con ID '" + id + "' no encontrada")
        );

        recipe.setName(recipeDto.getName());
        recipe.setShortDescription(recipeDto.getShortDescription());
        recipe.setFullDescription(recipeDto.getFullDescription());
        recipe.setCategory(recipeDto.getCategory());

        // --- INGREDIENTES ---
        List<Ingredient> existingIngredients = repo.findAllIngredientsByRecipeId(id); // Referencia original de Hibernate
        int i = 0;

        for (IngredientDto dto : recipeDto.getIngredients()) {
            Ingredient ingredient;
            if (i < existingIngredients.size()) {
                ingredient = existingIngredients.get(i);
                ingredient.setActive(true); // Reactivar si estaba inactivo
            } else {
                ingredient = new Ingredient();
                ingredient.setRecipe(recipe);
                existingIngredients.add(ingredient);
            }
            ingredient.setIngredientText(dto.getIngredientText());
            i++;
        }

        // Desactivar sobrantes
        for (; i < existingIngredients.size(); i++) {
            existingIngredients.get(i).setActive(false);
        }

        // --- PASOS ---
        List<Step> existingSteps = repo.findAllStepsByRecipeId(id); // Referencia original de Hibernate
        i = 0;

        for (StepDto dto : recipeDto.getSteps()) {
            Step step;
            if (i < existingSteps.size()) {
                step = existingSteps.get(i);
                step.setActive(true); // Reactivar si estaba inactivo
            } else {
                step = new Step();
                step.setRecipe(recipe);
                existingSteps.add(step);
            }
            step.setInstruction(dto.getInstruction());
            step.setStepOrder(i + 1);
            i++;
        }

        // Desactivar pasos sobrantes
        for (; i < existingSteps.size(); i++) {
            existingSteps.get(i).setActive(false);
        }

        Recipe updatedRecipe = repo.save(recipe);
        return ResponseEntity.ok(updatedRecipe);
    }


    // Este metodo maneja las peticiones DELETE a: localhost:8080/recipes
    // DELETE: soft delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Recipe> deleteRecipe(@PathVariable Long id) {
        Recipe recipe = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Receta con ID '" + id + "' no encontrada"));

        // Soft delete
        recipe.setActive(false);

        // Desactivar pasos e ingredientes relacionados
        recipe.getIngredients().forEach(i -> i.setActive(false));
        recipe.getSteps().forEach(s -> s.setActive(false));

        // Eliminar imagen física
        if (recipe.getImageFilename() != null) {
            Path oldImagePath = Paths.get("public/images/recipes/" + recipe.getImageFilename());
            try {
                Files.deleteIfExists(oldImagePath);
            } catch (IOException e) {
                System.err.println("Error al eliminar la imagen: " + e.getMessage());
            }
            // Poner el campo a null
            recipe.setImageFilename(null);
        }

        repo.save(recipe);
        return ResponseEntity.ok(recipe);
    }

    // Metodo auxiliar para actualizar pasos e ingredientes
    private void updateRecipeDetails(Recipe recipe, RecipeDto dto) {
        // Ingredientes
        List<Ingredient> existingIngredients = recipe.getIngredients();
        int size = Math.min(existingIngredients.size(), dto.getIngredients().size());
        for (int i = 0; i < size; i++) {
            Ingredient ing = existingIngredients.get(i);
            ing.setIngredientText(dto.getIngredients().get(i).getIngredientText());
            ing.setActive(true);
        }
        // Si llegan más ingredientes de los que existen → crear nuevos
        for (int i = size; i < dto.getIngredients().size(); i++) {
            IngredientDto ingDto = dto.getIngredients().get(i);
            Ingredient newIng = new Ingredient();
            newIng.setIngredientText(ingDto.getIngredientText());
            newIng.setRecipe(recipe);
            newIng.setActive(true);
            existingIngredients.add(newIng);
        }
        // Si hay menos → desactivar sobrantes
        for (int i = dto.getIngredients().size(); i < existingIngredients.size(); i++) {
            existingIngredients.get(i).setActive(false);
        }

        // Pasos
        List<Step> existingSteps = recipe.getSteps();
        size = Math.min(existingSteps.size(), dto.getSteps().size());
        for (int i = 0; i < size; i++) {
            Step step = existingSteps.get(i);
            StepDto stepDto = dto.getSteps().get(i);
            step.setInstruction(stepDto.getInstruction());
            step.setStepOrder(stepDto.getStepOrder());
            step.setActive(true);
        }
        // Crear nuevos
        for (int i = size; i < dto.getSteps().size(); i++) {
            StepDto stepDto = dto.getSteps().get(i);
            Step newStep = new Step();
            newStep.setInstruction(stepDto.getInstruction());
            newStep.setStepOrder(stepDto.getStepOrder());
            newStep.setRecipe(recipe);
            newStep.setActive(true);
            existingSteps.add(newStep);
        }
        // Desactivar sobrantes
        for (int i = dto.getSteps().size(); i < existingSteps.size(); i++) {
            existingSteps.get(i).setActive(false);
        }
    }

}
