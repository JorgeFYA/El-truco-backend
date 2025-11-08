package com.marketminds.ecommerce.elTruco.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredients")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Ingredients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String ingredientText;

    // RELACIÓN N:1 con Recipe
    // Muchos ingredientes pertenecen a una sola receta.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false) // Mapea la clave foránea (FK)
    @JsonIgnore // Ignora a la Receta al convertir ingredientes a JSON, porque la Receta principal ya se está convirtiendo a sí misma.
    private Recipes recipe;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;
}