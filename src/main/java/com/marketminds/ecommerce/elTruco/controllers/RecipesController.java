package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.dtos.RecipeDto;
import com.marketminds.ecommerce.elTruco.models.Recipe;
import com.marketminds.ecommerce.elTruco.services.RecipesService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@CrossOrigin(origins = {
        "http://127.0.0.1:5501",
        "https://jorgefya.github.io/el-truco/"
})
public class RecipesController {

    private final RecipesService recipesService;

    @Autowired
    public RecipesController(RecipesService recipesService) {
        this.recipesService = recipesService;
    }

    @GetMapping({"", "/"})
    public List<Recipe> getAllRecipes() {
        return recipesService.getAllActive();
    }

    @GetMapping("/{recipeSlug}")
    public Recipe getBySlug(@PathVariable("recipeSlug") String slug) {
        return recipesService.getBySlug(slug);
    }

    @GetMapping("/admin/{recipeId}")
    public Recipe getByIdAdmin(@PathVariable("recipeId") Long id) {
        return recipesService.getByIdAdmin(id);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Recipe> addRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        Recipe saved = recipesService.createRecipe(recipeDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/{recipeId}/upload-image")
    public ResponseEntity<Recipe> uploadImage(@PathVariable("recipeId") Long id, @RequestParam("image") MultipartFile file) {
        Recipe updated = recipesService.uploadImage(id, file);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable("recipeId") Long id, @Valid @RequestBody RecipeDto recipeDto) {
        Recipe updated = recipesService.updateRecipe(id, recipeDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Recipe> deleteRecipe(@PathVariable("recipeId") Long id) {
        Recipe deleted = recipesService.deleteRecipe(id);
        return ResponseEntity.ok(deleted);
    }
}
