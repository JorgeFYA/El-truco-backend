package com.marketminds.ecommerce.elTruco.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StepsDto {

    @NotNull(message = "El número de paso es requerido")
    @Min(value = 1, message = "El número de paso debe ser al menos 1")
    private Integer stepOrder;

    @NotEmpty(message = "La instrucción del paso no puede estar vacía")
    private String instruction;
}