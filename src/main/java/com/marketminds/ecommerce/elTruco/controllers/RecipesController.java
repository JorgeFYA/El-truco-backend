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

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    @GetMapping({"", "/"})
    public List<Recipe> getRecipeList () {
        // Usa el repositorio para obtener todas las recetas de MySQL
        // List<Recipe> recipes = repo.findAll();
        // Devuelve la lista. Spring Boot se encarga de convertir List<Recipe> a JSON.
        return repo.findAll();
    }

    // Endpoint para mostrar recetas individuales
    @GetMapping("/{slug}")
    public Recipe getRecipeBySlug(@PathVariable String slug) {

        // Llama al repositorio para buscar el Optional
        Optional<Recipe> recipe = repo.findBySlug(slug);

        // Si el Optional está vacío, lanza la excepción.
        return recipe.orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Receta no encontrada con slug: " + slug
        ));
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody RecipeDto recipeDto) {

        Recipe recipe = new Recipe();
        recipe.setName(recipeDto.getName());
        recipe.setShortDescription(recipeDto.getShortDescription());
        recipe.setFullDescription(recipeDto.getFullDescription());
        recipe.setCategory(recipeDto.getCategory());

        List<Ingredient> ingredientsEntities = new ArrayList<>();
        for (IngredientDto dto : recipeDto.getIngredients()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientText(dto.getIngredientText());
            ingredient.setRecipe(recipe);
            ingredientsEntities.add(ingredient);
        }
        recipe.setIngredients(ingredientsEntities);

        List<Step> stepEntities = new ArrayList<>();
        for (StepDto dto : recipeDto.getSteps()) {
            Step step = new Step();
            step.setStepOrder(dto.getStepOrder());
            step.setInstruction(dto.getInstruction());
            step.setRecipe(recipe);
            stepEntities.add(step);
        }
        recipe.setSteps(stepEntities);

        try {
            Recipe savedRecipe = repo.save(recipe);
            return new ResponseEntity<>(savedRecipe, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al agregar la receta: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<Recipe> uploadImage(@PathVariable Long id, @RequestParam("image")MultipartFile file) {
        Recipe recipe = repo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Receta con ID '" + id + "' no encontrada"
        ));

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Archivo de imagen vacío");
        }

        String storageFilename = "";
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

    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe( @PathVariable Long id, @Valid @RequestBody RecipeDto recipeDto) {

        Recipe recipe = repo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Receta con ID '" + id + "' no encontrada"
        ));

        recipe.setName(recipeDto.getName());
        recipe.setShortDescription(recipeDto.getShortDescription());
        recipe.setFullDescription(recipeDto.getFullDescription());
        recipe.setCategory(recipeDto.getCategory());

        recipe.getIngredients().clear();
        for (IngredientDto dto : recipeDto.getIngredients()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientText(dto.getIngredientText());
            ingredient.setRecipe(recipe);
            recipe.getIngredients().add(ingredient);
        }

        recipe.getSteps().clear();
        for (StepDto dto : recipeDto.getSteps()) {
            Step step = new Step();
            step.setStepOrder(dto.getStepOrder());
            step.setInstruction(dto.getInstruction());
            step.setRecipe(recipe);
            recipe.getSteps().add(step);
        }

        Recipe updatedRecipe = repo.save(recipe);
        return ResponseEntity.ok(updatedRecipe);
    }

}
