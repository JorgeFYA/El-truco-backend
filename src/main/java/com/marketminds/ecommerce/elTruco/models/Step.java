package com.marketminds.ecommerce.elTruco.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "steps")
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Recipe recipe;

    @Column(nullable = false)
    private boolean active = true;
}