package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.dtos.ProductVariantRequest;
import com.marketminds.ecommerce.elTruco.models.ProductVariant;
import com.marketminds.ecommerce.elTruco.models.Products;
import com.marketminds.ecommerce.elTruco.services.ProductVariantService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/productVariants")
@AllArgsConstructor
public class ProductVariantController {

    private final ProductVariantService productVariantService;

    @PostMapping
    public Products addProductVariantToProduct(@RequestParam(name = "productId", required = true) Long productId, @RequestBody ProductVariantRequest productVariantRequest){
        return productVariantService.addProductVariant(productId, productVariantRequest);
    }

    @GetMapping
    public List<ProductVariant> getProductVariantsByProductId (@RequestParam(required = true)Long productId){
        return this.productVariantService.getProductVariantByProductId(productId);
    }

    @GetMapping(path = "/single")
    public ProductVariant getProductVariantById(@RequestParam(name = "id", required = true)Long id, @RequestParam(name = "productId", required = true)Long productId){
        return productVariantService.getProductVariantById(productId,id);
    }

    @DeleteMapping
    public ProductVariant deleteProductVariantById(@RequestParam(name = "id", required = true)Long id, @RequestParam(name = "productId", required = true)Long productId){
        return productVariantService.deleteProductVariantById(productId, id);
    }

    @PutMapping
    public ProductVariant updateProductVariant (@RequestParam(name = "id", required = true)Long id,@RequestParam(name = "productId", required = true) Long productId,  @RequestBody ProductVariantRequest productVariantRequest) {
        return productVariantService.updateProductVariant(productId,id, productVariantRequest );
    }
}
