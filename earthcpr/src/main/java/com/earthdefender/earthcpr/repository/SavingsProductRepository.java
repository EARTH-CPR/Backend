package com.earthdefender.earthcpr.repository;

import com.earthdefender.earthcpr.model.SavingsProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavingsProductRepository extends JpaRepository<SavingsProduct, Long> {
    Optional<SavingsProduct> findByAccountTypeUniqueNo(String accountTypeUniqueNo);
}
