package com.marketminds.ecommerce.elTruco.services;

import com.marketminds.ecommerce.elTruco.models.Products;
import com.marketminds.ecommerce.elTruco.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {

    private final ProductsRepository productsRepository;

    //inyeccion de dependecias
    @Autowired
    public ProductsService(ProductsRepository productsRepository){
        this.productsRepository = productsRepository;
    }

    //read
    public List<Products> getAllProducts()
    {
        return productsRepository.findAll();
    }

    public Products getProductById(Long id){
        return productsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("El producto con el id: " + id + "no existe")
        );
    }

    public Products addProduct(Products product){
        return productsRepository.save(product);
    }

    public Products updateProductById(Long id, Products updatedProduct){
        Optional<Products> optionalProduct = productsRepository.findById(id);
        if(optionalProduct.isEmpty()) throw new IllegalArgumentException("El producto con el id: " + id + " no existe");
        Products productDB = optionalProduct.get();
        if(updatedProduct.getName() != null) productDB.setName(updatedProduct.getName());
        if(updatedProduct.getDescription() != null) productDB.setDescription(updatedProduct.getDescription());
        if(updatedProduct.getCategory() != null) productDB.setCategory(updatedProduct.getCategory());
        if(updatedProduct.getBasePrice() != null) productDB.setBasePrice(updatedProduct.getBasePrice());
        if(updatedProduct.getMainImage() != null) productDB.setMainImage(updatedProduct.getMainImage());
        if(updatedProduct.getActive() != null) productDB.setActive(updatedProduct.getActive());
        return productsRepository.save(productDB);
    }

    public Products deleteProductById(Long id){
        Products tmp = null;
        if(!productsRepository.existsById(id))return tmp;
        tmp = productsRepository.findById(id).get();
        productsRepository.deleteById(id);
        return tmp;
    }
}
