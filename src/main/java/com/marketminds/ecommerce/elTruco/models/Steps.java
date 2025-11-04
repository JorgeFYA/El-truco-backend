package com.marketminds.ecommerce.elTruco.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "steps")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Steps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    // mapea a la columna 'orden' o 'step_order'
    @Column(nullable = false)
    private Integer stepOrder;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String instruction;

    // RELACIÓN N:1 con Recipe
    // Muchos pasos pertenecen a una recta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false) // fk
    @JsonIgnore // Ignora a la Receta al convertir pasos a JSON, porque la Receta principal ya se está convirtiendo a sí misma.
    private Recipes recipe;

    @Column(nullable = false)
    private boolean active = true;
}