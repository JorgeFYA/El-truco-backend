package com.marketminds.ecommerce.elTruco.models;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientDto {

    @NotEmpty(message = "El texto del ingrediente no puede estar vacío")
    private String ingredientText;
}