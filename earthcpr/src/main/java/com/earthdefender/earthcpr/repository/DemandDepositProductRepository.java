package com.earthdefender.earthcpr.repository;

import com.earthdefender.earthcpr.model.DemandDepositProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DemandDepositProductRepository extends JpaRepository<DemandDepositProduct, Long> {
    Optional<DemandDepositProduct> findByAccountTypeUniqueNo(String accountTypeUniqueNo);

}