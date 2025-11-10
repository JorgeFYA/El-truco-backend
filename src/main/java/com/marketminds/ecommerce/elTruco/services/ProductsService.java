package com.marketminds.ecommerce.elTruco.services;

import com.marketminds.ecommerce.elTruco.models.Products;
import com.marketminds.ecommerce.elTruco.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
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

    public Products uploadImage(Long id, MultipartFile file) {
        Products product = productsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Producto con ID '" + id + "' no encontrada"));

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Archivo vacío");
        }

        String uploadDir = "src/main/resources/static/images/products/";
        String storageFilename = new Date().getTime() + "-" + file.getOriginalFilename();

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Borrar imagen anterior
            if (product.getMainImage() != null) {
                Files.deleteIfExists(Paths.get(uploadDir + product.getMainImage()));
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir + storageFilename), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al guardar imagen: " + e.getMessage());
        }

        product.setMainImage(storageFilename);
        return productsRepository.save(product);
    }

    public Products deleteProductById(Long id){
        Products tmp = null;
        if(!productsRepository.existsById(id))return tmp;
        tmp = productsRepository.findById(id).get();
        productsRepository.deleteById(id);
        return tmp;
    }
}
