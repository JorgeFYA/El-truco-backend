package com.marketminds.ecommerce.elTruco.repositories;


import com.marketminds.ecommerce.elTruco.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductsRepository extends JpaRepository <Products, Long> {
}
