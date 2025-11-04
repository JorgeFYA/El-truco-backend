package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.dtos.RecipesDto;
import com.marketminds.ecommerce.elTruco.models.Recipes;
import com.marketminds.ecommerce.elTruco.services.RecipesService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@AllArgsConstructor
public class RecipesController {

    private final RecipesService recipesService;

    @GetMapping({"", "/"})
    public List<Recipes> getAllRecipes() {
        return recipesService.getAllActive();
    }

    @GetMapping("/{recipeSlug}")
    public Recipes getBySlug(@PathVariable("recipeSlug") String slug) {
        return recipesService.getBySlug(slug);
    }

    @GetMapping("/admin/{recipeId}")
    public Recipes getByIdAdmin(@PathVariable("recipeId") Long id) {
        return recipesService.getByIdAdmin(id);
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Recipes> addRecipe(@Valid @RequestBody RecipesDto recipeDto) {
        Recipes saved = recipesService.createRecipe(recipeDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/{recipeId}/upload-image")
    public ResponseEntity<Recipes> uploadImage(@PathVariable("recipeId") Long id, @RequestParam("image") MultipartFile file) {
        Recipes updated = recipesService.uploadImage(id, file);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipes> updateRecipe(@PathVariable("recipeId") Long id, @Valid @RequestBody RecipesDto recipeDto) {
        Recipes updated = recipesService.updateRecipe(id, recipeDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Recipes> deleteRecipe(@PathVariable("recipeId") Long id) {
        Recipes deleted = recipesService.deleteRecipe(id);
        return ResponseEntity.ok(deleted);
    }
}
