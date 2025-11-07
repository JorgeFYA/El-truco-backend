package com.marketminds.ecommerce.elTruco.services;


import com.marketminds.ecommerce.elTruco.dtos.DirectionRequest;
import com.marketminds.ecommerce.elTruco.dtos.ProductVariantRequest;
import com.marketminds.ecommerce.elTruco.models.Directions;
import com.marketminds.ecommerce.elTruco.models.ProductVariant;
import com.marketminds.ecommerce.elTruco.models.Products;
import com.marketminds.ecommerce.elTruco.repositories.ProductVariantRepository;
import com.marketminds.ecommerce.elTruco.repositories.ProductsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductVariantService {
    private final ProductsRepository productsRepository;
    private final ProductVariantRepository productVariantRepository;

    public Products addProductVariant(Long productId, ProductVariantRequest productVariantRequest){
        Products product = productsRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("No existe el producto con el id: " + productId)
        );
        ProductVariant productVariant = new ProductVariant();
        if(productVariantRequest.getName() != null) productVariant.setName(productVariantRequest.getName());
        if(productVariantRequest.getPrice() != null) productVariant.setPrice(productVariantRequest.getPrice());
        if(productVariantRequest.getStock() != null) productVariant.setStock(productVariantRequest.getStock());
        if(productVariantRequest.getImage() != null) productVariant.setImage(productVariantRequest.getImage());

        //Asociamos el producto con la variante
        productVariant.setProduct(product);
        //Guardamos la variante en la base de datos
        productVariantRepository.save(productVariant);
        //Guardamos la variante en el list del producto
        product.getVariants().add(productVariant);
        //Actualizamos el usuario en la base de datos
        return productsRepository.save(product);
    }

    public List<ProductVariant> getProductVariantByProductId(Long productId){
        Optional<Products> optionalProduct = productsRepository.findById(productId);
        if(optionalProduct.isEmpty()) throw new IllegalArgumentException("No existe el producto con el id: " + productId);
        Products product = optionalProduct.get();
        return product.getVariants();
    }

    public ProductVariant getProductVariantById(Long productId, Long id){
        if(!productsRepository.existsById(productId)) throw new IllegalArgumentException("No existe el producto con el id " + productId);
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(
                () ->  new IllegalArgumentException("No existe una variante con el id " + id)
        );
        if(!productVariant.getProduct().getId().equals(productId))throw new IllegalArgumentException("El id del producto no coincide con el de la variante") ;
        return productVariant;
    }

    public ProductVariant deleteProductVariantById(Long productId, Long id){
        if(!productsRepository.existsById(productId)) throw new IllegalArgumentException("No existe el producto con el id " + productId);
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(
                () ->  new IllegalArgumentException("No existe una variante con el id " + id)
        );
        if(!productVariant.getProduct().getId().equals(productId))throw new IllegalArgumentException("El id de la variante no coincide con el del producto") ;
        productVariantRepository.deleteById(id);
        return productVariant;
    }

    public ProductVariant updateProductVariant(Long productId,Long id, ProductVariantRequest productVariantRequest){
        if(!productsRepository.existsById(productId)) throw new IllegalArgumentException("No existe el producto con el id " + productId);
        ProductVariant productVariant = productVariantRepository.findById(id).orElseThrow(
                () ->  new IllegalArgumentException("No existe una variante con el id " + id)
        );
        if(!productVariant.getProduct().getId().equals(productId))throw new IllegalArgumentException("El id de la variante no coincide con el del producto") ;
        if(productVariantRequest.getName() != null) productVariant.setName(productVariantRequest.getName());
        if(productVariantRequest.getPrice() != null) productVariant.setPrice(productVariantRequest.getPrice());
        if(productVariantRequest.getStock() != null) productVariant.setStock(productVariantRequest.getStock());
        if(productVariantRequest.getImage() != null) productVariant.setImage(productVariantRequest.getImage());
        return productVariantRepository.save(productVariant);
    }

}
