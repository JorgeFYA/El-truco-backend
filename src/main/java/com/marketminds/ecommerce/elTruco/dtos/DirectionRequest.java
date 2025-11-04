package com.marketminds.ecommerce.elTruco.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectionRequest {
    private String street;
    private String suburb;
    private String country;
    private String zipCode;
}
