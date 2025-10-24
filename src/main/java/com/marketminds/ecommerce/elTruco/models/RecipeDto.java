package com.marketminds.ecommerce.elTruco.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeDto {

    private String slug;

    @NotEmpty(message = "El nombre de la receta no puede estar vacío")
    private String name;

    @NotEmpty(message = "La descripción corta no puede estar vacía")
    @Size(min = 10, message = "La descripción corta debe ser de al menos 10 caracteres")
    @Size(max = 120, message = "La descripción corta no puede ser mayor a 120 caracteres")
    private String shortDescription;

    @NotEmpty(message = "La descripción no puede estar vacía")
    @Size(min = 10, message = "La descripción debe ser de al menos 10 caracteres")
    @Size(max = 2000, message = "La descripción no puede ser mayor a 2000 caracteres")
    private String fullDescription;

    @NotEmpty(message = "Es necesario seleccionar una categoría")
    private String category;

    @Valid
    @NotEmpty(message = "La receta debe tener al menos un ingrediente")
    private List<IngredientDto> ingredients;

    @Valid
    @NotEmpty(message = "La receta debe tener al menos un paso")
    private List<StepDto> steps;

}
