package com.marketminds.ecommerce.elTruco.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ingredientText;

    // RELACIÓN N:1 con Recipe
    // Muchos ingredientes pertenecen a una sola receta.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false) // Mapea la clave foránea (FK)
    @JsonIgnore // Ignora a la Receta al convertir ingredientes a JSON, porque la Receta principal ya se está convirtiendo a sí misma.
    private Recipe recipe;

    @Column(nullable = false)
    private boolean active = true;
}