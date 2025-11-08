package com.marketminds.ecommerce.elTruco.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recipes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Recipes {

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String shortDescription;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String fullDescription;

    private String imageFilename;

    private String category;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    // RELACIONES 1:N
    // Un cambio en la receta afecta a los ingredientes (CASCADE)
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @SQLRestriction("active = true")
    private List<Ingredients> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    // @OrderBy para que los pasos se carguen en el orden correcto
    @SQLRestriction("active = true")
    @OrderBy("stepOrder ASC")
    private List<Steps> steps = new ArrayList<>();

    public void setName(String name) {
        this.name = name;

        this.slug = slugify(name);
    }

    public String slugify(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        String noAccents = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}", "");

        String lower = noAccents.toLowerCase();

        lower = lower.replaceAll("ñ", "n");

        String alphaNumeric = lower.replaceAll("[^a-z0-9\\s-]", "");

        String slug = alphaNumeric.replaceAll("[\\s-]+", "-");

        return slug.replaceAll("^-|-$", "");
    }
}