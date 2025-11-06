package com.marketminds.ecommerce.elTruco.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductVariantRequest {
    private String name;
    private Double price;
    private String image;
    private Integer stock;


}
