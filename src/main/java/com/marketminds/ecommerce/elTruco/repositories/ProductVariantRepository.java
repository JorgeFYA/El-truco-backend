package com.marketminds.ecommerce.elTruco.repositories;

import com.marketminds.ecommerce.elTruco.models.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository <ProductVariant, Long> {
}
