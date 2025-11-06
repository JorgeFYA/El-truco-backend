package com.marketminds.ecommerce.elTruco.controllers;

import com.marketminds.ecommerce.elTruco.models.Products;
import com.marketminds.ecommerce.elTruco.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/products")
public class ProductsController {
    public final ProductsService productsService;

    @Autowired

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping //http:/localhost:8080/api/products
    public List<Products> getAllProducts(){
        return productsService.getAllProducts();
    }

    @GetMapping(path = "/{productId}") //http:/localhost:8080/api/products/2
    public Products getProductById(@PathVariable("productId")Long id){
        return productsService.getProductById(id);
    }

    @PostMapping
    public Products addProduct(@RequestBody Products product){
        return productsService.addProduct(product);
    }

    @DeleteMapping (path = "/{productId}")
    public Products deleteProductById(@PathVariable("productId")Long id){
        return productsService.deleteProductById(id);
    }

    @PutMapping("/{productId}")
    public Products updateProduct(@PathVariable("productId")Long id, @RequestBody Products product){
        return productsService.updateProductById(id,product);
    }

}
