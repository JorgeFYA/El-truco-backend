package com.marketminds.ecommerce.elTruco.services;

import com.marketminds.ecommerce.elTruco.dtos.IngredientDto;
import com.marketminds.ecommerce.elTruco.dtos.RecipeDto;
import com.marketminds.ecommerce.elTruco.dtos.StepDto;
import com.marketminds.ecommerce.elTruco.models.Ingredient;
import com.marketminds.ecommerce.elTruco.models.Recipe;
import com.marketminds.ecommerce.elTruco.models.Step;
import com.marketminds.ecommerce.elTruco.repositories.RecipesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
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

@Service
public class RecipesService {

    private final RecipesRepository repo;

    @Autowired
    public RecipesService(RecipesRepository repo) {
        this.repo = repo;
    }

    public List<Recipe> getAllActive() {
        return repo.findAllActive();
    }

    public Recipe getBySlug(String slug) {
        Recipe recipe = repo.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Receta no encontrada con slug: " + slug));

        if (!recipe.isActive()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Receta inactiva");
        }

        return recipe;
    }

    public Recipe getByIdAdmin(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Receta no encontrada con ID: " + id));
    }

    public Recipe createRecipe(RecipeDto dto) {
        String slug = new Recipe().slugify(dto.getName());
        Recipe recipe = repo.findBySlug(slug)
                .filter(r -> !r.isActive())
                .orElse(new Recipe());

        recipe.setName(dto.getName());
        recipe.setShortDescription(dto.getShortDescription());
        recipe.setFullDescription(dto.getFullDescription());
        recipe.setCategory(dto.getCategory());
        recipe.setActive(true);

        updateRecipeDetails(recipe, dto);

        return repo.save(recipe);
    }

    public Recipe updateRecipe(Long id, RecipeDto dto) {
        Recipe recipe = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Receta con ID '" + id + "' no encontrada"));

        recipe.setName(dto.getName());
        recipe.setShortDescription(dto.getShortDescription());
        recipe.setFullDescription(dto.getFullDescription());
        recipe.setCategory(dto.getCategory());

        updateRecipeDetails(recipe, dto);

        return repo.save(recipe);
    }

    public Recipe uploadImage(Long id, MultipartFile file) {
        Recipe recipe = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Receta con ID '" + id + "' no encontrada"));

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Archivo vacío");
        }

        String uploadDir = "public/images/recipes/";
        String storageFilename = new Date().getTime() + "-" + file.getOriginalFilename();

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Borrar imagen anterior
            if (recipe.getImageFilename() != null) {
                Files.deleteIfExists(Paths.get(uploadDir + recipe.getImageFilename()));
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFilename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al guardar imagen: " + e.getMessage());
        }

        recipe.setImageFilename(storageFilename);
        return repo.save(recipe);
    }

    public Recipe deleteRecipe(Long id) {
        Recipe recipe = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Receta no encontrada"));

        recipe.setActive(false);
        recipe.getIngredients().forEach(i -> i.setActive(false));
        recipe.getSteps().forEach(s -> s.setActive(false));

        if (recipe.getImageFilename() != null) {
            try {
                Files.deleteIfExists(Paths.get("public/images/recipes/" + recipe.getImageFilename()));
            } catch (IOException e) {
                System.err.println("Error al eliminar imagen: " + e.getMessage());
            }
            recipe.setImageFilename(null);
        }

        return repo.save(recipe);
    }

    // Lógica de sincronización de pasos e ingredientes
    private void updateRecipeDetails(Recipe recipe, RecipeDto dto) {
        List<Ingredient> ingredients = recipe.getIngredients();
        int size = Math.min(ingredients.size(), dto.getIngredients().size());

        for (int i = 0; i < size; i++) {
            Ingredient ing = ingredients.get(i);
            ing.setIngredientText(dto.getIngredients().get(i).getIngredientText());
            ing.setActive(true);
        }

        for (int i = size; i < dto.getIngredients().size(); i++) {
            IngredientDto ingDto = dto.getIngredients().get(i);
            Ingredient newIng = new Ingredient();
            newIng.setIngredientText(ingDto.getIngredientText());
            newIng.setRecipe(recipe);
            newIng.setActive(true);
            ingredients.add(newIng);
        }

        for (int i = dto.getIngredients().size(); i < ingredients.size(); i++) {
            ingredients.get(i).setActive(false);
        }

        List<Step> steps = recipe.getSteps();
        size = Math.min(steps.size(), dto.getSteps().size());

        for (int i = 0; i < size; i++) {
            Step step = steps.get(i);
            StepDto dtoStep = dto.getSteps().get(i);
            step.setInstruction(dtoStep.getInstruction());
            step.setStepOrder(dtoStep.getStepOrder());
            step.setActive(true);
        }

        for (int i = size; i < dto.getSteps().size(); i++) {
            StepDto stepDto = dto.getSteps().get(i);
            Step newStep = new Step();
            newStep.setInstruction(stepDto.getInstruction());
            newStep.setStepOrder(stepDto.getStepOrder());
            newStep.setRecipe(recipe);
            newStep.setActive(true);
            steps.add(newStep);
        }

        for (int i = dto.getSteps().size(); i < steps.size(); i++) {
            steps.get(i).setActive(false);
        }
    }
}
